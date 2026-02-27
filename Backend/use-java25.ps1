# Utiliser Java 25 pour Maven dans cette session PowerShell
# Exécutez: . .\use-java25.ps1

$candidates = @()
$candidates += Get-Item "C:\Program Files\Java\jdk-25" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Java\jdk-25*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Eclipse Adoptium\jdk-25*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Microsoft\jdk-25*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "$env:USERPROFILE\.jdks\*25*" -ErrorAction SilentlyContinue
$jdk25 = $candidates | Where-Object { $_.PSIsContainer } | Select-Object -First 1 -ExpandProperty FullName

if ($jdk25) {
    $env:JAVA_HOME = $jdk25
    $env:Path = "$jdk25\bin;" + $env:Path
    Write-Host "JAVA_HOME = $env:JAVA_HOME"
    java -version
} else {
    Write-Host "JDK 25 non trouvé. Installez-le ou modifiez les chemins dans ce script."
}
