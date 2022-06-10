package io.libs

// Создает базу в кластере через RAS или пакетный режим. Для пакетного режима есть возможность создать базу с конфигурацией
//
// Параметры:
//  platform - номер платформы 1С, например 8.3.12.1529
//  server1c - сервер 1c
//  serversql - сервер 1c 
//  base - имя базы на сервере 1c и sql
//  cfdt - файловый путь к dt или cf конфигурации для загрузки. Только для пакетного режима!
//  isras - если true, то используется RAS для скрипта, в противном случае - пакетный режим

def creating1cBase(infobase, local) {
    utils = new Utils()

    utils.powershell("Remove-Item -Recurse -Force -Path \"${local}/test2 ")

    utils.cmd("\"${path1c}\" CREATEINFOBASE FILE=\"${local}/${infobase}\" ")
}




// Убирает в 1С базу окошки с тем, что база перемещена, интернет поддержкой, очищает настройки ванессы
//
// Параметры:
//  сonnection_string - путь к 1С базе.
//  admin1cUsr - имя админа 1С базы
//  admin1cPwd - пароль админа 1С базы
//
def unlocking1cBase(connString, admin1cUsr, admin1cPwd) {
    utils = new Utils()

    admin1cUsrLine = ""
    if (admin1cUser != null && !admin1cUser.isEmpty()) {
        admin1cUsrLine = "--db-user ${admin1cUsr}"
    }

    admin1cPwdLine = ""
    if (admin1cPwd != null && !admin1cPwd.isEmpty()) {
        admin1cPwdLine = "--db-pwd ${admin1cPwd}"
    }

    utils.cmd("runner run --execute ${env.WORKSPACE}/one_script_tools/unlockBase1C.epf --command \"-locktype unlock\" ${admin1cUsrLine} ${admin1cPwdLine} --ibconnection=${connString}")
}



def getConnString1(local, infobase) {
    return "/F${local}\\${infobase}"
}

// Загружает в базу конфигурацию из 1С хранилища. Базу желательно подключить к хранилищу под загружаемым пользователем,
//  т.к. это даст буст по скорости загрузки.
//
// Параметры:
//
//
def loadCfgFrom1CStorage(infobase, admin1cUser, admin1cPassword, platform, gitpath, path1c) {
    utils = new Utils()

    returnCode = utils.cmd("rd /s/q \"${env.WORKSPACE}/confs")


    returnCode = utils.cmd("git clone ${gitpath} \"${env.WORKSPACE}/confs")
    if (returnCode != 0) {
         utils.raiseError("Загрузка конфигурации из github  ${infobase} завершилась с ошибкой. ")
    }

    returnCode = utils.cmd("\"${path1c}\" DESIGNER /F${local}/${infobase}  /LoadConfigFromFiles ${env.WORKSPACE}\\confs")
    if (returnCode != 0) {
         utils.raiseError("Загрузка конфигурации из папки \"${env.WORKSPACE}/confs завершилась с ошибкой.")
    }

}

// Обновляет базу в режиме конфигуратора. Аналог нажатия кнопки f7
//
// Параметры:
//
//  connString - строка соединения, например /Sdevadapter\template_adapter_adapter
//  platform - полный номер платформы 1с
//  admin1cUser - администратор базы
//  admin1cPassword - пароль администратора базы
//
def updateInfobase(connString, admin1cUser, admin1cPassword, platform) {

    utils = new Utils()
    admin1cUserLine = "";
    if (!admin1cUser.isEmpty()) {
        admin1cUserLine = "--db-user ${admin1cUser}"
    }
    admin1cPassLine = "";
    if (!admin1cPassword.isEmpty()) {
        admin1cPassLine = "--db-pwd ${admin1cPassword}"
    }
    platformLine = ""
    if (platform != null && !platform.isEmpty()) {
        platformLine = "--v8version ${platform}"
    }

    returnCode = utils.cmd("runner updatedb --ibconnection ${connString} ${admin1cUserLine} ${admin1cPassLine} ${platformLine}")
    if (returnCode != 0) {
        utils.raiseError("Обновление базы ${connString} в режиме конфигуратора завершилось с ошибкой. Для дополнительной информации смотрите логи")
    }
}
