package bbct.android.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class FormFieldError(var message: String? = null, var isValid: Boolean = true)

data class DetailsErrors(
    var condition: FormFieldError = FormFieldError(),
    var brand: FormFieldError = FormFieldError(),
    var year: FormFieldError = FormFieldError(),
    var number: FormFieldError = FormFieldError(),
    var value: FormFieldError = FormFieldError(),
    var quantity: FormFieldError = FormFieldError(),
    var playerName: FormFieldError = FormFieldError(),
    var team: FormFieldError = FormFieldError(),
    var position: FormFieldError = FormFieldError(),
) {
    val isValid: Boolean
        get() {
            return condition.isValid && brand.isValid && year.isValid && number.isValid && value.isValid && quantity.isValid && playerName.isValid && team.isValid && position.isValid
        }
}

class DetailsViewModel : ViewModel() {
    var detailsState = mutableStateOf(DetailsState())
    var errors = mutableStateOf(DetailsErrors())

    fun validate() {
        val baseballCard = detailsState.value
        val errors = DetailsErrors()

        if (baseballCard.condition.isBlank()) {
            errors.condition = FormFieldError("Condition is required")
            errors.condition.isValid = false
        }

        if (baseballCard.brand.isBlank()) {
            errors.brand = FormFieldError("Brand is required")
            errors.brand.isValid = false
        }

        if (baseballCard.year.isBlank()) {
            errors.year = FormFieldError("Year is required")
            errors.year.isValid = false
        }

        if (baseballCard.year.toIntOrNull() == null) {
            errors.year = FormFieldError("Year must be a number")
            errors.year.isValid = false
        }

        if (baseballCard.number.isBlank()) {
            errors.number = FormFieldError("Number is required")
            errors.number.isValid = false
        }

        if (baseballCard.value != null && baseballCard.value?.toDoubleOrNull() == null) {
            errors.value = FormFieldError("Value must be a number")
            errors.value.isValid = false
        }

        if (baseballCard.quantity.isBlank()) {
            errors.quantity = FormFieldError("Quantity is required")
            errors.quantity.isValid = false
        }

        if (baseballCard.quantity.toIntOrNull() == null) {
            errors.quantity = FormFieldError("Quantity must be a number")
            errors.quantity.isValid = false
        }

        if (baseballCard.playerName.isBlank()) {
            errors.playerName = FormFieldError("Player name is required")
            errors.playerName.isValid = false
        }

        if (baseballCard.team.isBlank()) {
            errors.team = FormFieldError("Team is required")
            errors.team.isValid = false
        }

        if (baseballCard.position.isBlank()) {
            errors.position = FormFieldError("Position is required")
            errors.position.isValid = false
        }

        this.errors.value = errors
    }
}
