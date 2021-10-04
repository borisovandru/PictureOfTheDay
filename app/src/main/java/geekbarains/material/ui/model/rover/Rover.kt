package geekbarains.material.ui.model.rover

data class Rover(
    val name: String,
    val dateStart: String,
    val dateEnd: String,
    val cameras: List<String>
)