# Utiliser Java 21+ pour Maven (selenium-tests compile en Java 21)
# Exécutez: . .\use-java.ps1   puis   mvn test

$candidates = @()
$candidates += Get-Item "C:\Program Files\Java\jdk-25" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Java\jdk-2*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Eclipse Adoptium\jdk-2*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "C:\Program Files\Microsoft\jdk-2*" -ErrorAction SilentlyContinue
$candidates += Get-ChildItem "$env:USERPROFILE\.jdks\*" -ErrorAction SilentlyContinue | Where-Object { $_.Name -match "21|25" }
$jdk = $candidates | Where-Object { $_.PSIsContainer } | Select-Object -First 1 -ExpandProperty FullName

if ($jdk) {
    $env:JAVA_HOME = $jdk
    $env:Path = "$jdk\bin;" + $env:Path
    Write-Host "JAVA_HOME = $env:JAVA_HOME"
    java -version
} else {
    Write-Host "JDK 21 ou 25 non trouvé. Installez-en un ou lancez ce script depuis Backend après .\use-java25.ps1"
}
