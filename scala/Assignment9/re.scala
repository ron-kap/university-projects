// Part 1 about Regular Expression Matching
//==========================================

// Regular Expressions
abstract class Rexp
case object ZERO extends Rexp
case object ONE extends Rexp
case class CHAR(c: Char) extends Rexp
case class ALT(r1: Rexp, r2: Rexp) extends Rexp   // alternative 
case class SEQ(r1: Rexp, r2: Rexp) extends Rexp   // sequence
case class STAR(r: Rexp) extends Rexp             // star


// some convenience for typing regular expressions

import scala.language.implicitConversions    
import scala.language.reflectiveCalls 

def charlist2rexp(s: List[Char]): Rexp = s match {
  case Nil => ONE
  case c::Nil => CHAR(c)
  case c::s => SEQ(CHAR(c), charlist2rexp(s))
}
implicit def string2rexp(s: String): Rexp = charlist2rexp(s.toList)

implicit def RexpOps (r: Rexp) = new {
  def | (s: Rexp) = ALT(r, s)
  def % = STAR(r)
  def ~ (s: Rexp) = SEQ(r, s)
}

implicit def stringOps (s: String) = new {
  def | (r: Rexp) = ALT(s, r)
  def | (r: String) = ALT(s, r)
  def % = STAR(s)
  def ~ (r: Rexp) = SEQ(s, r)
  def ~ (r: String) = SEQ(s, r)
}

// (1) Complete the function nullable according to
// the definition given in the coursework; this 
// function checks whether a regular expression
// can match the empty string and Returns a boolean
// accordingly.

    // DONE
def nullable (r: Rexp) : Boolean = r match {
  case ZERO => false
  case ONE => true
  case CHAR(a) => false
  case ALT(a, b) => (nullable(a) || nullable(b))
  case SEQ(a, b) => (nullable(a) && nullable(b))
  case STAR(a) => true
}

/*
nullable(ZERO) == false
nullable(ONE) == true
nullable(CHAR('a')) == false
nullable(ZERO | ONE) == true
nullable(ZERO | CHAR('a')) == false
nullable(ONE ~ ONE) == true
nullable(ONE ~ CHAR('a')) == false
nullable(STAR(ZERO)) == true
*/

// (2) Complete the function der according to
// the definition given in the coursework; this
// function calculates the derivative of a 
// regular expression w.r.t. a character.

    // ????
def der(c: Char, r: Rexp) : Rexp = r match {
  case ZERO => ZERO
  case ONE => ZERO
  case CHAR(d) => if (c == d) ONE else ZERO
  case ALT(a, b) => ALT(der(c, a), der(c, b))
  case SEQ(a, b) => if (nullable(a)) ALT(SEQ(der(c,a), b), der(c, b)) else SEQ(der(c, a), b)
  case STAR(d) => SEQ(der(c, d), STAR(d))
}

/*
der('a', ZERO | ONE) == (ZERO | ZERO)
der('a', (CHAR('a') | ONE) ~ CHAR('a')) == ALT((ONE | ZERO) ~ CHAR('a'), ONE)
der('a', STAR(CHAR('a'))) == (ONE ~ STAR(CHAR('a')))
der('b', STAR(CHAR('a'))) == (ZERO ~ STAR(CHAR('a')))
der('b', "abc")         // stackoverflow case
der('a', EVIL)
*/

// (3) Complete the simp function according to
// the specification given in the coursework; this
// function simplifies a regular expression from
// the inside out, like you would simplify arithmetic 
// expressions; however it does not simplify inside 
// STAR-regular expressions.

    // DONE
def simp(r: Rexp) : Rexp = r match {
  case ZERO => ZERO
  case ONE => ONE
  case CHAR(c) => CHAR(c)
  case ALT(a, b) => (simp(a), simp(b)) match {
    case (first, ZERO) => first
    case (ZERO, second) => second
    case (first, second) => if ((first == second)) first else ALT(first, second)
  }
  case SEQ(a, b) => (simp(a), simp(b)) match {
    case (_, ZERO) => ZERO
    case (ZERO, _) => ZERO
    case (first, ONE) => first
    case (ONE, second) => second
    case (first, second) => SEQ(first, second)
  }
  case STAR(x) => STAR(x)
}

/*
simp(ZERO | ONE) == ONE
simp(STAR(ZERO | ONE)) == STAR(ZERO | ONE)
simp(ONE ~ (ONE ~ (ONE ~ CHAR('a')))) == CHAR('a')
simp(ONE ~ (ONE ~ (ONE ~ ZERO))) == ZERO
simp(ALT(ONE ~ (ONE ~ (ONE ~ ZERO)), CHAR('a'))) == CHAR('a')
simp(CHAR('a') | CHAR('a')) == CHAR('a')
simp(ONE | CHAR('a')) == (ONE | CHAR('a'))
simp(ALT((CHAR('a') | ZERO) ~ ONE, ((ONE | CHAR('b')) | CHAR('c')) ~ (CHAR('d') ~ ZERO))) == CHAR('a')
*/

// (4) Complete the two functions below; the first 
// calculates the derivative w.r.t. a string; the second
// is the regular expression matcher taking a regular
// expression and a string and checks whether the
// string matches the regular expression

    // DONE
def ders (s: List[Char], r: Rexp) : Rexp = s match {
  case Nil => r
  case c::cs => ders(cs, simp(der(c, r)))
}

    // DONE
def matcher(r: Rexp, s: String): Boolean = {
  nullable(ders(s.toList, r))
}

/*
val EVIL = SEQ(STAR(STAR(CHAR('a'))), CHAR('b'))
ders("aaaaa".toList, EVIL) == SEQ(SEQ(STAR(CHAR('a')),STAR(STAR(CHAR('a')))),CHAR('b'))
ders(List('b'), EVIL) == ONE
ders("bb".toList, EVIL) == ZERO

matcher(((CHAR('a') ~ CHAR('b')) ~ CHAR('c')), "abc");

matcher(EVIL, "a" * 5 ++ "b") == true
matcher(EVIL, "b") == true
matcher(EVIL, "bb") == false
matcher("abc", "abc") == true                             //  These two cases run into stackoverflow, i think because of char and string conversion,
matcher(("ab" | "a") ~ (ONE | "bc"), "abc") == true       //  no tail rec, pat match is not solution for der()
matcher(ONE, "") == true
matcher(ZERO, "") == false
matcher(ONE | CHAR('a'), "") == true
matcher(ONE | CHAR('a'), "a") == true
*/

// (5) Complete the size function for regular
// expressions according to the specification 
// given in the coursework.

    // DONE
def size(r: Rexp): Int = r match {
  case ZERO => 1
  case ONE => 1
  case CHAR(c) => 1
  case ALT(a, b) => 1 + size(a) + size(b)
  case SEQ(a, b) => 1 + size(a) + size(b)
  case STAR(r) => 1 + size(r)
}


// some testing data

/*
matcher(("a" ~ "b") ~ "c", "abc")  // => true
matcher(("a" ~ "b") ~ "c", "ab")   // => false

// the supposedly 'evil' regular expression (a*)* b
val EVIL = SEQ(STAR(STAR(CHAR('a'))), CHAR('b'))

matcher(EVIL, "a" * 1000 ++ "b")   // => true
matcher(EVIL, "a" * 1000)          // => false

// size without simplifications
size(der('a', der('a', EVIL)))             // => 28
size(der('a', der('a', der('a', EVIL))))   // => 58

// size with simplification
size(simp(der('a', der('a', EVIL))))           // => 8
size(simp(der('a', der('a', der('a', EVIL))))) // => 8
simp(der('a', der('a', der('a', EVIL))))

// Python needs around 30 seconds for matching 28 a's with EVIL. 
// Java 9 and later increase this to an "astonishing" 40000 a's in
// 30 seconds.
//
// Lets see how long it really takes to match strings with 
// 5 Million a's...it should be in the range of a couple
// of seconds.

def time_needed[T](i: Int, code: => T) = {
  val start = System.nanoTime()
  for (j <- 1 to i) code
  val end = System.nanoTime()
  (end - start)/(i * 1.0e9)
}

for (i <- 0 to 5000000 by 500000) {
  println(i + " " + "%.5f".format(time_needed(2, matcher(EVIL, "a" * i))))
}

// another "power" test case 
simp(Iterator.iterate(ONE:Rexp)(r => SEQ(r, ONE | ONE)).drop(50).next) == ONE

// the Iterator produces the rexp
//
//      SEQ(SEQ(SEQ(..., ONE | ONE) , ONE | ONE), ONE | ONE)
//
//    where SEQ is nested 50 times.

*/

