package org.Alura_alu_games

data class InfoJogo(val info: InfoApiShark) {
    override fun toString(): String {
        return info.toString()
    }
}