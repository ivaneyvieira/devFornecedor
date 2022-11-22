#!/bin/bash

DIR=`pwd`

find $DIR/pedidosPDF/*.pdf | while read ARQ_PDF
do
  BASE_NAME=`basename "$ARQ_PDF" .pdf`
  ./pdf/pdftotext -table  -enc 'UTF-8' "$ARQ_PDF"  "$DIR/pedidosPDF/$BASE_NAME.txt"
  sed -r '/^[ ]*$/d'  -i "$DIR/pedidosPDF/$BASE_NAME.txt"
done