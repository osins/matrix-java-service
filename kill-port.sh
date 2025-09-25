#!/bin/bash

PORT=${1:-9090}

echo "查找占用端口 $PORT 的进程..."

PID=$(lsof -t -i :$PORT)

if [ -z "$PID" ]; then
  echo "端口 $PORT 没有被占用。"
  exit 0
fi

echo "找到占用端口 $PORT 的进程 PID：$PID"
echo "正在杀死进程..."

kill -9 $PID

if [ $? -eq 0 ]; then
  echo "进程 $PID 已成功杀死。"
else
  echo "杀死进程失败，请检查权限或进程状态。"
fi
