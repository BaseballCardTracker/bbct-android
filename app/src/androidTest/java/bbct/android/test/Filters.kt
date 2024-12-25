package bbct.android.test

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher


fun hasRole(role: Role): SemanticsMatcher {
    return SemanticsMatcher("hasRole $role") {
        it.config.getOrNull(SemanticsProperties.Role) == role
    }
}
