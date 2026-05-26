BurgerFit - Sistema de Gestion para Cadena de Comida

Integrantes:
Samuel Moncayo
Felipe Arancibia

Proposito del proyecto:
El objetivo de este sistema es resolver la gestion operativa de BurgerFit, una cadena de venta de hamburguesas que cuenta con multiples sucursales. El proyecto permite centralizar y controlar en tiempo real tres areas criticas del negocio: el registro de los clientes frecuentes, la administracion del menu con sus precios y stock, y la recepcion de los pedidos de comida que ingresan a cada local.

Explicacion de la solucion:
Para que el sistema sea eficiente y no se caiga si se llena de pedidos, se diseño de forma dividida mediante una arquitectura de microservicios. Esto significa que la aplicacion se compone de tres modulos independientes que trabajan en equipo:

1. Modulo de Clientes: Controla las cuentas, los datos de contacto y el ingreso de los usuarios.
2. Modulo de Productos: Gestiona el catalogo del menu, los valores de cada combo y la disponibilidad de los ingredientes en cocina.
3. Modulo de Pedidos: Se encarga de procesar las compras de los clientes y asignarlas a la sucursal correspondiente para su preparacion.

Cada uno de estos modulos fue programado usando Spring Boot con lenguaje Java, y mantiene su propia base de datos Postgres independiente a traves de Docker para asegurar que la informacion de los pedidos no se mezcle con la de los clientes.

Forma de uso en la presentacion:
El proyecto viene listo para ser clonado desde este repositorio. Para probar el funcionamiento, se deben levantar las bases de datos en Docker Desktop y ejecutar las tres aplicaciones en paralelo desde IntelliJ. Las pruebas de los flujos de compra, creacion y busqueda de datos se realizan directamente disparando las solicitudes que dejamos preparadas dentro del archivo de Postman adjunto en la carpeta raiz.
