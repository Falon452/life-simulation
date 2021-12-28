package agh.ics.oop.classes

/** Parses string of format " (1,2) (3,4) (5,6)  "to list<Vector2d>
*
* returns null or list<Vector2d>
 *     */

class PositionParser {
    fun parse(input: String): List<Vector2d>? {
        var input = input.trimStart()
        input = input.trimEnd()
        val splitInput = input.split(" ")

        val output = mutableListOf<Vector2d>()

        splitInput.forEach {
            val tuple = it.split(",")
            if (tuple.size < 2)
                return null
            
            val a1 = tuple[0].substring(1).toIntOrNull()
            val a2 = tuple[1].substringBefore(")").toIntOrNull()

            if (a1 != null && a2 != null)
                output.add(Vector2d(a1, a2))
             else
                return null
        }
        return output
    }
}
