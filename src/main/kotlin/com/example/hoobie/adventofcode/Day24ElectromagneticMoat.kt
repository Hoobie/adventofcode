package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil
import com.example.hoobie.adventofcode.utils.tail
import com.google.common.base.Objects

/*
--- Day 24: Electromagnetic Moat ---
The CPU itself is a large, black building surrounded by a bottomless pit. 
Enormous metal tubes extend outward from the side of the building at regular intervals and descend down into the void. 
There's no way to cross, but you need to get inside.

No way, of course, other than building a bridge out of the magnetic components strewn about nearby.

Each component has two ports, one on each end. The ports come in all different types, 
and only matching types can be connected. You take an inventory of the components by their port types (your puzzle input). 
Each port is identified by the number of pins it uses; more pins mean a stronger connection for your bridge. 
A 3/7 component, for example, has a type-3 port on one side, and a type-7 port on the other.

Your side of the pit is metallic; a perfect surface to connect a magnetic, zero-pin port. 
Because of this, the first port you use must be of type 0. It doesn't matter what type of port you end with; 
your goal is just to make the bridge as strong as possible.

The strength of a bridge is the sum of the port types in each component. For example, 
if your bridge is made of components 0/3, 3/7, and 7/4, your bridge has a strength of 0+3 + 3+7 + 7+4 = 24.

For example, suppose you had the following components:

0/2
2/2
2/3
3/4
3/5
0/1
10/1
9/10
With them, you could make the following valid bridges:

0/1
0/1--10/1
0/1--10/1--9/10
0/2
0/2--2/3
0/2--2/3--3/4
0/2--2/3--3/5
0/2--2/2
0/2--2/2--2/3
0/2--2/2--2/3--3/4
0/2--2/2--2/3--3/5
(Note how, as shown by 10/1, order of ports within a component doesn't matter. 
However, you may only use each port on a component once.)

Of these bridges, the strongest one is 0/1--10/1--9/10; it has a strength of 0+1 + 1+10 + 10+9 = 31.

What is the strength of the strongest bridge you can make with the components you have available?

--- Part Two ---
The bridge you've built isn't long enough; you can't jump the rest of the way.

In the example above, there are two longest bridges:

0/2--2/2--2/3--3/4
0/2--2/2--2/3--3/5
Of them, the one which uses the 3/5 component is stronger; its strength is 0+2 + 2+2 + 2+3 + 3+5 = 19.

What is the strength of the longest bridge you can make? If you can make multiple bridges of the longest length, 
pick the strongest one.
 */
object Day24ElectromagneticMoat {

    fun getStrength(input: String): Int {
        val inventory = input.split("\n")
                .flatMap {
                    val split = it.split("/")
                    listOf(Component(split[0].toInt(), split[1].toInt()))
                }

        val init = listOf(listOf(inventory.find { it.inPins == 0 }!!))

        val bridges = buildBridges(init, inventory.filter { it.inPins != 0 })

        return countMaxStrength(listOf(), bridges)
    }

    private tailrec fun buildBridges(bridges: List<List<Component>>, inventory: List<Component>, idx: Int = 0,
                                     bridgesHashes: Set<Int> = hashSetOf()): List<List<Component>> {
        if (idx >= inventory.size) return bridges

        val (newBridges, newBridgesHashes) = buildBridge(bridges, inventory, bridgesHashes)

        val maxSize = bridges.maxBy { it.size }!!.size
        val newBridgesFiltered = newBridges.filter { it.size > maxSize - inventory.size / 5 } // hack to make it faster

        return buildBridges(newBridgesFiltered, inventory, idx + 1, newBridgesHashes)
    }

    private tailrec fun buildBridge(bridges: List<List<Component>>, inventory: List<Component>,
                                    bridgesHashes: Set<Int>): Pair<List<List<Component>>, Set<Int>> {
        if (inventory.isEmpty()) return Pair(bridges, bridgesHashes)

        val (newBridges, newBridgesHashes) = applyComponent(bridges, 0, inventory.first(), bridgesHashes)

        return buildBridge(newBridges, inventory.tail(), newBridgesHashes)
    }

    private tailrec fun applyComponent(bridges: List<List<Component>>, idx: Int, component: Component,
                                       bridgesHashes: Set<Int>): Pair<List<List<Component>>, Set<Int>> {
        if (idx >= bridges.size) return Pair(bridges, bridgesHashes)

        val bridge: List<Component> = addComponent(bridges[idx], component)

        val modified = bridges[idx].size != bridge.size
        val bridgeHash = bridge.hashCode()
        val notContains = bridgeHash !in bridgesHashes

        val newBridgesHashes = if (modified && notContains) bridgesHashes + bridgeHash else bridgesHashes
        val newBridges = if (modified && notContains) bridges + listOf(bridge) else bridges
        val newIdx = if (modified && notContains) idx + 2 else idx + 1

        return applyComponent(newBridges, newIdx, component, newBridgesHashes)
    }

    private fun addComponent(bridge: List<Component>, component: Component): List<Component> {
        val (matches, inverted) = bridge.last().matches(component)
        return if (matches && component !in bridge) bridge + Component(component.inPins, component.outPins, inverted) else bridge
    }

    private tailrec fun countMaxStrength(strengths: List<Int>, bridges: List<List<Component>>): Int {
        if (bridges.isEmpty()) return strengths.max() ?: 0

        val newStrengths = if (bridges.first().first().inPins == 0)
            strengths + countStrength(0, bridges.first()) else strengths
        return countMaxStrength(newStrengths, bridges.tail())
    }

    private tailrec fun countStrength(strength: Int, bridge: List<Component>): Int {
        if (bridge.isEmpty()) return strength

        return countStrength(strength + bridge.first().getStrength(), bridge.tail())
    }

    private data class Component(val inPins: Int, val outPins: Int, val inverted: Boolean = false) {
        fun matches(other: Component): Pair<Boolean, Boolean> {
            if (this == other) return Pair(false, false)

            if (inverted) {
                if (inPins == other.inPins) return Pair(true, false)
                if (inPins == other.outPins) return Pair(true, true)
            } else {
                if (outPins == other.inPins) return Pair(true, false)
                if (outPins == other.outPins) return Pair(true, true)
            }

            return Pair(false, false)
        }

        fun getStrength() = this.inPins + this.outPins

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null) return false
            if (javaClass != other.javaClass) return false
            val component = other as Component
            return Objects.equal(inPins, component.inPins) && Objects.equal(outPins, component.outPins)
        }

        override fun hashCode(): Int {
            return Objects.hashCode(inPins, outPins)
        }
    }

}

private val inputFileName = "day24.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Strength: " + Day24ElectromagneticMoat.getStrength(input))
}

