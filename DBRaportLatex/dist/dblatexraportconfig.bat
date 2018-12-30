
#dbLatexRaportConfig#WYGENEROWANY CONFIG
 #WYGENEROWANY CONFIG
#TO JEST PRZYKLADOWY KOMENTARZ NIE BRANY POD UWAGE
#PONIZEJ SA WSZYSTKIE WARTOSCI KTORE NALEZY ZMIENIC:
#szablony:

templatepath=szablony/
output=wyniki/
encodingtex=UTF-8


#OBSLUGIWANE dbengine: firebirdsql, mysql,sqlite
#dbpath oznacza rowniez dbname w przypadku mysql
#BAZA DANYCH POLACZENIE:

dbengine=firebirdsql
hostname=//localhost
port=3050
dbpath=D:/test.fdb
user=SYSDBA
password=masterkey
dbencoding=WIN1250


#TEX KOMPILATOR:

pdflatexpath=texlive/2014/bin/win32/lualatex.exe
#pdfcompilemainfile nazwy plikow do kompilacji po przecinkach
#lub ALL lub NONE lub ONLYBEGDOC gdzie skompilowane zostana tylko dokumenty zawierajace begindocument i enddocument
pdfcompilemainfile=main.txt
