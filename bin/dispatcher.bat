@echo off
set "ROOT=%~dp0../"
set "classpath=%ROOT%lib\*:%ROOT%WEB-INF\lib\*:%ROOT%WEB-INF\classes":%classpath%
@java -cp "%ROOT%lib\*;%ROOT%WEB-INF\lib\*;%ROOT%WEB-INF\classes" org.tinystruct.system.Dispatcher %*