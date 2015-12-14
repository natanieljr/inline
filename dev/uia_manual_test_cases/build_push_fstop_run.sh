#!/bin/bash
./build_push.sh
./fstop.sh $3
./run.sh $1 $2