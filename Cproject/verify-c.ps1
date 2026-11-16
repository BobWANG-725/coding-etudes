# Verify the C toolchain used by VS Code in this folder.
# Run this FILE, do not paste its contents into the console:
#   cd 'D:\Coding Exercises\Cproject'
#   powershell -ExecutionPolicy Bypass -File .\verify-c.ps1

$ErrorActionPreference = 'Stop'

$root = $PSScriptRoot
if (-not $root) { $root = Split-Path -Parent $MyInvocation.MyCommand.Path }
if (-not $root) { $root = (Get-Location).Path }
if (-not (Test-Path (Join-Path $root 'hello.c'))) {
    $fallbackRoot = 'D:\Coding Exercises\Cproject'
    if (Test-Path (Join-Path $fallbackRoot 'hello.c')) { $root = $fallbackRoot }
}
if (-not (Test-Path (Join-Path $root 'hello.c'))) {
    Write-Host "[FAIL] hello.c not found under $root" -ForegroundColor Red
    Write-Host 'Run this script from the Cproject folder:'
    Write-Host '  cd ''D:\Coding Exercises\Cproject'''
    Write-Host '  powershell -ExecutionPolicy Bypass -File .\verify-c.ps1'
    exit 1
}

function Find-Tool($name, $fallback) {
    $cmd = Get-Command $name -ErrorAction SilentlyContinue
    if ($cmd) { return $cmd.Source }
    if (Test-Path $fallback) { return $fallback }
    return $null
}

$gcc = Find-Tool 'gcc' 'C:\msys64\ucrt64\bin\gcc.exe'
if (-not $gcc) {
    Write-Host '[FAIL] gcc not found.' -ForegroundColor Red
    Write-Host 'Install MSYS2 UCRT64 first, or add C:\msys64\ucrt64\bin to PATH.'
    Write-Host 'See the setup guide in the Cproject folder.'
    exit 1
}

Write-Host "[OK] gcc: $gcc"
& $gcc --version | Select-Object -First 1

$gdb = Find-Tool 'gdb' 'C:\msys64\ucrt64\bin\gdb.exe'
if ($gdb) {
    Write-Host "[OK] gdb: $gdb"
    & $gdb --version | Select-Object -First 1
} else {
    Write-Host '[WARN] gdb not found. Compiling works, debugging in VS Code will not.' -ForegroundColor Yellow
}

$source = Join-Path $root 'hello.c'
$exe = Join-Path $env:TEMP 'coding-etudes-hello.exe'
if (-not (Test-Path $source)) {
    Write-Host "[FAIL] source not found: $source" -ForegroundColor Red
    exit 1
}

if (Test-Path $exe) {
    Remove-Item $exe -Force -ErrorAction SilentlyContinue
}

Write-Host ''
Write-Host "Compiling $source -> $exe ..."
& $gcc -std=c99 -Wall -Wextra -g $source -o $exe
if ($LASTEXITCODE -ne 0) {
    Write-Host '[FAIL] compilation failed.' -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host ''
Write-Host "Running $exe ..."
& $exe
if ($LASTEXITCODE -ne 0) {
    Write-Host '[FAIL] program exited with a non-zero code.' -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host ''
Write-Host '[OK] C toolchain is ready.' -ForegroundColor Green
