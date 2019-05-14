/*
 * Minecraft Dev for IntelliJ
 *
 * https://minecraftdev.org
 *
 * Copyright (c) 2019 minecraft-dev
 *
 * MIT License
 */

package com.demonwav.mcdev.i18n.reference

import com.demonwav.mcdev.util.mcPath
import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonProperty
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext

class I18nJsonReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(JsonElementTypes.PROPERTY),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    if (element.containingFile.virtualFile?.mcPath?.startsWith("lang/") == false) {
                        return arrayOf()
                    }
                    val entry = element as? JsonProperty ?: return arrayOf()
                    return arrayOf(
                        I18nReference(
                            element,
                            entry.nameElement.textRangeInParent.shiftRight(1).grown(-2),
                            true,
                            entry.name,
                            entry.name
                        ) { elem, _, newName ->
                            (elem as JsonProperty).setName(newName)
                        }
                    )
                }
            }
        )
    }
}