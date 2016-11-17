title CsmRefreshDb

@echo off
echo Start...

pushd %~dp0

cd ..
call git pull
call mvn antrun:run -PrefreshDb

popd
pause
