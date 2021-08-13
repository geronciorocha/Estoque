@echo off
set programa=BD.exe
tasklist /FI "IMAGENAME eq BD.exe "2>NUL | find /I /N"BD.exe">NUL
if "%ERRORLEVEL%"=="0" (
echo " <Software><software>BD</software><executando>true</executando></Software> "  >"verificaExecucao.xml"
) else (
" <Software><software>BD</software><executando>false</executando></Software> " 
)
pause > nul