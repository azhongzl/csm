title CsmRefreshDbWithTest

@echo off
echo Start...

pushd %~dp0

cd ..
call git pull
call mvn antrun:run -PrefreshDbWithTest

popd
pause
