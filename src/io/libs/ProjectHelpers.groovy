package io.libs

def creating1cBase(infobase, local, deleteornot) {
    if (deleteornot != null && !deleteornot.isEmpty() && deleteornot =="нет"){

    }
    
    else if (deleteornot == "да") {
        utils = new Utils()

        utils.powershell("Remove-Item -Recurse -Force -Path \"${local}/${infobase}\" ")

        utils.cmd("\"${path1c}\" CREATEINFOBASE FILE=\"${local}/${infobase}\" ")

    }
    
}
// Убирает в 1С базу окошки с тем, что база перемещена, интернет поддержкой, очищает настройки ванессы
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

def loadCfgFrom1CStorage(infobase, admin1cUser, admin1cPassword, platform, gitpath, path1c) {
    utils = new Utils()

    returnCode = utils.cmd("rd /s/q \"${env.WORKSPACE}/confs/${infobase}")

    returnCode = utils.cmd("git clone ${gitpath} \"${env.WORKSPACE}/confs/${infobase}")
    if (returnCode != 0) {
         utils.raiseError("Загрузка конфигурации из github  ${infobase} завершилась с ошибкой. ")
    }

    if (admin1cPassword != null && !admin1cPassword.isEmpty()) {
        returnCode = utils.cmd("\"${path1c}\" DESIGNER /F${local}/${infobase}  /LoadConfigFromFiles ${env.WORKSPACE}\\confs\\${infobase} /N ${admin1cUser} /P ${admin1cPassword} ")
        if (returnCode != 0) {
            utils.raiseError("Загрузка конфигурации из папки \"${env.WORKSPACE}/confs завершилась с ошибкой.")}
                
    } else {
        returnCode = utils.cmd("\"${path1c}\" DESIGNER /F${local}/${infobase}  /LoadConfigFromFiles ${env.WORKSPACE}\\confs\\${infobase} /N ${admin1cUser}")
        if (returnCode != 0) {
            utils.raiseError("Загрузка конфигурации из папки \"${env.WORKSPACE}/confs завершилась с ошибкой.")
    }
    }
}
// Обновляет базу в режиме конфигуратора. Аналог нажатия кнопки f7
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
