# Digital Gateway - README Técnico

Este documento describe aspectos técnicos clave del **Digital Gateway**, incluyendo el manejo de ISO 8583 y la arquitectura de comunicación con el CORE.

---

## 1. Estructura del Bitmap ISO 8583

**Pregunta:**  
### ¿Cómo está estructurado tu bitmap ISO 8583?
<!--  -->
El Bitmap ISO8583 se compone del MTI (indica qué operación se va a realizar), y luego hay 2 bitmaps. Si en el primer bitmap, la primera letra o número (que se encuentra en hexadecimal), al convertirlo a binario, es un 0, significa que a partir de la siguiente línea ya son los campos presentes en la trama. Si en cambio, es un 1, significa que la siguiente línea es un 2do bitmap.
Los bitmaps nos indican qué campos están presentes y qué campos no lo están en la trama.
Mínimo hay 1 bitmap y máximo 2, cada bitmap son 64bits (lo que equivale a 63 campos en el primer bitmap ya que el primer número solo indica si hay un segundo bitmap) y el segundo bitmap nos indica los campos presentes desde el campo 64 hasta el 127.
Entonces, lo que hago es validar mis primeras 3 líneas para asegurarme que su longitud no sea menor a 3 dígitos ya que el MTI tiene una longitud de 4, el primer bitmap no baja de 16 su longitud y el segundo de igual forma.
En un caso real, habría que validar solo las 2 primeras líneas y a partir de la 3ra las validaciones no se aplican a la longitud, sino ya a los campos, pero en este caso me basé en el ejemplo propuesto en el documento de la prueba técnica.
Luego hago una conversión de los bitmaps hexadecimales a binario. Valido el primer bitmap y si su primer dígito es 1, entonces hago la conversión del segundo bitmap.
Luego de ello, hago un recorrido por los Data Elements (DE) y valido su longitud según su tipo. Para ello, cree un archivo en la carpeta config donde tengo la definición de los campos, su tipo y su longitud.
Con esto, una vez acabo de recorrer el mensaje ISO8583, creo un objeto java listo para ser transformado a JSON y devolverlo como respuesta al controllador, el que luego se encargará de enviar la respuesta al microservicio consumidor.
---

**Pregunta:**
### ¿Cómo determinas si un campo está presente?

**Respuesta:**
<!--  -->
En el bitmap, al transformar de hexadecimal a binario, si el resultado es 0, el campo no está presente, si el resultado 1 el campo si está presente y así vamos avanzando hasta terminar de recorrer el bitmap.
Los campos van en orden desde el primero hasta el número 128 (si es que hay 2 bitmaps). Una vez tienes mapeado cuantos bitmaps hay (1 o 2), comienzas desde la siguiente línea enumerando los Data Element (DE). El primer DE solo indica si hay otro bitmap o no, así que en la práctica no cuenta como DE, luego está el DE-2 que si ya es un campo.
Basicamente recorremos el número binario y si es un 1 y vamos en la 3ra vuelta, el DE-3 está presente, si todos son 0, entonces no hay ningún DE presente.
---

## 2. Parsing de Reglas de Producto

**Pregunta:**  
### ¿Qué estrategia usaste para el parsing XML de las reglas de producto? ¿JAXB, StAX, DOM? ¿Por qué?

**Respuesta:**
<!--  -->
Lamentablemente, por falta de tiempo no pude completar esta parte de la prueba técnica, pero me ha gustado así que, a pesar de que en media hora se acaba el tiempo para la entrega, el día de mañana estaré subiendo la implementación y actualizando este README, por si hay interés en la respuesta.
---

## 3. Concurrencia en el Cliente TCP

**Pregunta:**
### ¿Cómo manejas la concurrencia en el cliente TCP?

**Respuesta:**
<!--  -->
Para el cliente TCP la concurrencia la manejo mediante hilos. Cada hilo maneja la lectura y escritura de su operación de manera independiente, evitando el bloqueo de otras operaciones.
---
**Pregunta:**
### ¿Usas una conexión por request o un pool de conexiones?

**Respuesta:**
<!--  -->
Una conexión por request puede ser muy poco escalable y tiende a mayor consumo y latencia por lo que a gran escala representa un peor rendimiento. Procuro inclinarme más a usar un pool de conexiones la cual se puede reutilizar ahorrando recursos y reduciendo latencia lo que a gran escala se siente.
---

## 4. Consideraciones si el CORE fuera IBM MQ

**Pregunta:**
### ¿Qué harías diferente si el CORE usara IBM MQ en lugar de TCP directo?

**Respuesta:**
<!--  -->
En lugar de abrir sockets y manejar reconexiones, solo configuraría mi cliente para publicar mensajes en la cola correspondiente.
Tampoco me tendría que preocupar por perdida de mensajes, usando algún caché o disparando el consumo de CPU en caso el CORE está temporalmente fuera de línea, ya que esto lo gestionaría el servidor MQ y al superarse el incidente, el servidor MQ mantiene el historial de los mensajes.
---

## 5. Extensibilidad del Gateway

**Pregunta:** 
### ¿Cómo extenderías el Gateway para soportar un nuevo producto bancario sin modificar código Java?

**Respuesta:**
<!--  -->
Actualmente la forma en que lo manejo es mediante la creación de contratos con OpenApi. En este defino las reglas del producto, automatizaciones, códigos de error, canales dueños del producto y diferentes validaciones de seguridad. También tiene el plus de facilitar la generación de documentación.
---

