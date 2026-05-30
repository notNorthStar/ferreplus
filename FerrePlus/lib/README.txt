============================================================
Ferre+ - Librerias externas necesarias
Equipo 3: Sosa / Gutierrez
============================================================

Antes de abrir el proyecto en NetBeans hay que descargar 2 JARs
y colocarlos en esta carpeta (FerrePlus/lib/) con EXACTAMENTE
estos nombres:

  1) flatlaf-3.4.jar
     Look & Feel moderno para Swing.
     Descarga directa:
       https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4/flatlaf-3.4.jar

  2) mysql-connector-j-8.4.0.jar
     Driver JDBC para MySQL 8.
     Descarga directa:
       https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar

------------------------------------------------------------
Si NetBeans marca las librerias como "missing references":
  Click derecho en el proyecto -> Properties -> Libraries ->
  Compile -> Add JAR/Folder -> seleccionar los dos JARs.
------------------------------------------------------------

Base de datos:
  Antes de ejecutar la app, correr el script SQL:
    FerrePlus/sql/ferreplus.sql
  desde MySQL Workbench o consola:
    mysql -u root -p < FerrePlus/sql/ferreplus.sql

  Usuario de prueba:
    user: admin
    pass: admin123
