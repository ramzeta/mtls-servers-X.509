
# Seguridad X.509

Crear dos aplicaciones que se comuniquen utilizando TLS mutuo, cada una con su propio certificado digital.
El P12 es el almacen de claves que contiene la clave privada y el certificado digital.
El certificado digital es un archivo que contiene la clave publica y la informacion del propietario de la clave publica.


## Requisitos

- Tener Java descargado y configurado en la ruta PATH.

## Keystore para server1

Generar un par de claves para server1 y exportar el certificado:

```bash
keytool -genkeypair -alias server1 -keyalg RSA -keysize 2048 -dname "CN=server1" -validity 365 -storetype PKCS12 -keystore s1_ks.p12 -storepass admin123

keytool -exportcert -alias server1 -storetype PKCS12 -keystore s1_ks.p12 -file s1_cert.cer -rfc -storepass admin123
```

## Keystore para server2

Generar un par de claves para server2 y exportar el certificado:

```bash
keytool -genkeypair -alias server2 -keyalg RSA -keysize 2048 -dname "CN=server2" -validity 365 -storetype PKCS12 -keystore s2_ks.p12 -storepass admin123

keytool -exportcert -alias server2 -storetype PKCS12 -keystore s2_ks.p12 -file s2_cert.cer -rfc -storepass admin123
```

Importar el certificado de server2 en el almacen de claves del servidor 1 para que pueda confiar en el servidor 2:


## Truststore S1 IMPORT S2:

Importar el certificado de server2 al truststore de server1:
Este comando genera el truststore (almacen de confianza)  s1_ts.p12 con el certificado de server2.
```bash
keytool -importcert -alias server2 -storetype PKCS12 -keystore s1_ts.p12 -file s2_cert.cer -rfc -storepass admin123
```

Importar el certificado de server1 en el almacen de claves del servidor 2 para que pueda confiar en el servidor 1:
Este comando genera el truststore (almacen de confianza) s2_ts.p12 con el certificado de server1.


## Truststore S2 IMPORT S1:
```bash
keytool -importcert -alias server1 -storetype PKCS12 -keystore s2_ts.p12 -file s1_cert.cer -rfc -storepass admin123
```

Recuerda salir a la carpeta principal para ejecutar los comandos o moverlos arrastrando a cada proyecto.


## Preguntas frecuentes

¿Qué es un certificado digital?
Un certificado digital es un archivo que contiene la clave pública y la información del propietario de la clave pública.

¿Qué es un almacén de claves?
Un almacén de claves es un archivo que contiene claves privadas y certificados digitales.

¿Qué es un truststore?
Un truststore es un almacén de claves que contiene certificados digitales de entidades de confianza.

¿Qué es un keystore?
Un keystore es un almacén de claves que contiene claves privadas y certificados digitales.

¿Qué es un certificado autofirmado?
Un certificado autofirmado es un certificado digital que ha sido firmado por el mismo propietario de la clave pública.

¿Qué es una entidad certificadora?
Una entidad certificadora es una entidad que emite certificados digitales y los firma digitalmente.


https://github.com/Hakky54/sslcontext-kickstart


## Configurar el archivo hosts

### Windows
C:\Windows\System32\drivers\etc\hosts

### Linux
/etc/hosts

127.0.0.1 server1 server2