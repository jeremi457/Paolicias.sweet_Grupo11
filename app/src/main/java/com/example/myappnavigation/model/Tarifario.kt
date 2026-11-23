package com.example.myappnavigation.model

/**
 * Logica de tarificacion y presupuestos de eventos.
 *
 * Regla de negocio:
 *  1. Cada producto tiene un precio base por cada 10 personas.
 *  2. Las personas se eligen de 10 en 10, por lo que se forman "grupos de 10".
 *  3. Total = precio base x cantidad de grupos de 10.
 *  4. Abono = 50% del total.
 *
 * Ejemplo: precio base $25.000, 50 personas
 *   grupos = 50 / 10 = 5
 *   total  = 25.000 x 5 = $125.000
 *   abono  = 125.000 x 50% = $62.500
 */
object Tarifario {
    const val PASO = 10
    const val MAX_ASISTENTES = 100
    const val PORCENTAJE_ABONO = 50

    /** Desglose paso a paso del calculo, pensado para mostrarse al usuario. */
    data class Desglose(
        val precioBase: Int,
        val asistentes: Int,
        val grupos: Int,
        val total: Int,
        val abono: Int
    )

    /** Cantidad de grupos de 10 personas (50 personas = 5 grupos). */
    fun calcularGrupos(asistentes: Int): Int = asistentes / PASO

    /** Total = precio base x grupos de 10 personas. */
    fun calcularTotal(precioBase: Int, asistentes: Int): Int =
        precioBase * calcularGrupos(asistentes)

    /** Abono = 50% del total, requerido para confirmar y bloquear la fecha. */
    fun calcularAbono(total: Int): Int = total * PORCENTAJE_ABONO / 100

    fun desglosar(precioBase: Int, asistentes: Int): Desglose {
        val total = calcularTotal(precioBase, asistentes)
        return Desglose(
            precioBase = precioBase,
            asistentes = asistentes,
            grupos = calcularGrupos(asistentes),
            total = total,
            abono = calcularAbono(total)
        )
    }
}