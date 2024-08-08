:: Lance facilement les différents serveurs java nécessaires.

@echo OFF
cls

if not exist %cd%\build mkdir %cd%\build

javac -version
if %errorlevel% EQU 1 (
    echo Merci d'installer javac pour utiliser ce program 
    exit /b
) else (
    javac -d %cd%\build -cp %cd%\JAVA\misc\json.jar %cd%\JAVA\src\fr\corentin_owen\security\*.java %cd%\JAVA\src\fr\corentin_owen\delivery\*.java %cd%\JAVA\src\fr\corentin_owen\utils\*.java %cd%\JAVA\src\fr\corentin_owen\config\*.java %cd%\JAVA\src\fr\corentin_owen\car\*.java %cd%\JAVA\src\fr\corentin_owen\messages\*.java %cd%\JAVA\src\fr\corentin_owen\communications\http\handlers\*.java %cd%\JAVA\src\fr\corentin_owen\communications\http\*.java %cd%\JAVA\src\fr\corentin_owen\communications\*.java %cd%\JAVA\src\fr\corentin_owen\parking\*.java %cd%\JAVA\src\fr\corentin_owen\communications\udp\*.java
    start cmd.exe /K java -cp %cd%\JAVA\misc\*;%cd%\build\ fr.corentin_owen.communications.http.TeslaDealerShip
    start cmd.exe /K java -cp %cd%\JAVA\misc\*;%cd%\build\ fr.corentin_owen.communications.http.PeugeotDealerShip
    start cmd.exe /K java -cp %cd%\JAVA\misc\*;%cd%\build\ fr.corentin_owen.communications.TeslaFactory
    start cmd.exe /K java -cp %cd%\JAVA\misc\*;%cd%\build\ fr.corentin_owen.communications.PeugeotFactory
    start cmd.exe /K java -cp %cd%\JAVA\misc\*;%cd%\build\ fr.corentin_owen.communications.udp.TeslaProvider
    start cmd.exe /K java -cp %cd%\JAVA\misc\*;%cd%\build\ fr.corentin_owen.communications.udp.PeugeotProvider
    exit 0
)

Pause