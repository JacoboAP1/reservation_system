\- Validar que solo se permitan roles USER y ADMIN (Por el momento se están permitiendo más que esos dos) **LISTO**

\- Cambiar Javadoc a inglés **LISTO**

\- Link para abrir interfaz de Swagger: http://localhost:8080/swagger-ui/index.html

\- No olvidar cambiar de rama en cada implementación distinta

2️⃣ Gestión de usuarios (ADMIN) **TERMINADO**

(Rama: user-management)

Acciones:

- Ver usuarios

- Activar / desactivar usuario

- Eliminar usuario

Regla:

Solo ADMIN

3️⃣ Gestión de recursos

(Rama: resources)

Acciones:

- Crear recurso

- Listar recursos

- Activar / desactivar recurso

Regla:

Solo recursos activos se pueden reservar

4️⃣ Reservas (CORE DEL SISTEMA)

(Rama: reservations)

Acciones:

- Crear reserva

- Listar reservas del usuario

- Cancelar reserva

Reglas clave:

Un usuario solo puede tener una reserva activa

Un recurso no puede solaparse en el tiempo

Solo recursos activos

5️⃣ Validaciones (Service layer)

(No endpoints, pero sí lógica clave)

Validaciones:

- Usuario activo

- Recurso activo

- No solapamiento de fechas

- No doble reserva activa por usuario

👉 Esto vive en ReservationService

6️⃣ Auditoría mínima (opcional por ahora)

(Después)

Acciones:

- Fecha de creación

- Estado de reserva

- Ya lo tienes parcialmente:

    created_at

    status

Propuesta de ramas Git
main
├── auth
├── user-management
├── resources
├── reservations

Orden recomendado de desarrollo

✅ auth

✅ users + roles

✅ resources

✅ reservations (lo más pesado)

🔜 mejoras / refactor
