package day03

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import se.horv.day03.Day03

class Day03Test {

@Test
 fun part1() {

 val result = Day03().solve("""xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""
  .split("\n"), false)

 assertEquals("161", result)
 }

 @Test
 fun part2() {

  val result = Day03().solve("""xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""
   .split("\n"), true)

  assertEquals("48", result)
 }
}