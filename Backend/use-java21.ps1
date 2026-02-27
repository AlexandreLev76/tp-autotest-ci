# Utiliser Java 21 pour Maven dans cette session PowerShell
# Exécutez: . .\use-java21.ps1   (ou ajustez le chemin du JDK ci-dessous)

$candidates = @()
$candidates += Get-Item "C:\Program Files\Java\jdk-21" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Java\jdk-21*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Eclipse Adoptium\jdk-21*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Microsoft\jdk-21*" -ErrorAction SilentlyContinue
$jdk21 = $candidates | Where-Object { $_.PSIsContainer } | Select-Object -First 1 -ExpandProperty FullName

if ($jdk21) {
    $env:JAVA_HOME = $jdk21
    $env:Path = "$jdk21\bin;" + $env:Path
    Write-Host "JAVA_HOME = $env:JAVA_HOME"
    java -version
} else {
    Write-Host "JDK 21 non trouvé. Installez-le ou modifiez les chemins dans ce script."
}
