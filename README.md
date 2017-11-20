# ASM_Simulador
Simulador de un ensamblador en Java.

Proyecto para la clase de Estructura y programación de computadoras - Facultad de Ingeniería UNAM.

Es una versión funcional, aunque carece de un análisis semántico, por el momento.

Se trata de un lenguaje ensamblador con mnemónicos en español. Tenemos una interfaz gráfica donde podemos escribir código, ejecutarlo (ensamblarlo), guardarlo y abrir archivos. También se incluye una sección para ver el resultado de la ejecución y una tabla donde podemos ver el valor con el que terminan los registros al finalizar la ejecución.

# Estructura básica de un programa

-----------------------------------------

  .datos
  
INSTRUCCIONES

  FIN

-----------------------------------------

Donde ' .datos ' es una directiva y ' FIN ' es una palabra reservada.
  
# Conjunto de instrucciones

- Comentarios: Empiezan y terminan con un ' # '.
- Cadenas: Empiezan y terminan con un ' " '.
- Registros: $t0. El dígito puede ir del 0 al 9. Son registros de propósito general,
- (Cargar NUmero) cnu registro, valor. Ejemplos:  
  cnu $t1, 15.1  
  cnu $t0, -1  
- (Cargar STring) cst registro, valor. Ejemplos:   
  cst $t2, "HOLA"  
- (IMPrimir) imp [registro | valor]. Ejemplos:  
  imp $t1  
  imp 15.3  
  imp "Hola"  
- (SUMar) sum \$t1, \$t2, \$t3. El resultado se guarda en \$t3.  
- (REStar) res \$t1, \$t2, \$t3. El resultado se guarda en \$t3.  
- (MULtiplicar) mul \$t1, \$t2, \$t3. El resultado se guarda en \$t3.  
- (DIVidir) div \$t1, \$t2, \$t3. El resultado se guarda en \$t3.  
- (Si es Mayor, Salta) sms \$t1, \$t2, ETIQUETA. Compara si lo que hay en \$t1 es mayor a lo que hay en \$t2, de ser así, salta a ETIQUETA. En caso contrario, ignora la instrucción.  
- (Saltar) sal ETIQUETA. Salta a la etiqueta que la digamos.
