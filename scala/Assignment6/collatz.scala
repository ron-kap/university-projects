// Part 1 about the 3n+1 conjecture
//=================================


//(1) Complete the collatz function below. It should
//    recursively calculate the number of steps needed 
//    until the collatz series reaches the number 1.
//    If needed, you can use an auxiliary function that
//    performs the recursion. The function should expect
//    arguments in the range of 1 to 1 Million.


def collatz(n: Long) : Long = {

    def collatz_rec(n: Long, c: Long) : Long = if ((n % 2) == 0) collatz_rec(n/2, c + 1) else if (n == 1) c else collatz_rec(3 * (n) + 1, c + 1)

    collatz_rec(n, 0)
}


//(2)  Complete the collatz_max function below. It should
//     calculate how many steps are needed for each number 
//     from 1 up to a bound and then calculate the maximum number of
//     steps and the corresponding number that needs that many 
//     steps. Again, you should expect bounds in the range of 1
//     up to 1 Million. The first component of the pair is
//     the maximum number of steps and the second is the 
//     corresponding number.


def collatz_max(bnd: Long) : (Long, Long) = {
    val lst = for (n <- (1 to (bnd.toInt)).toList) yield (collatz(n), n.toLong)
    lst.max
}