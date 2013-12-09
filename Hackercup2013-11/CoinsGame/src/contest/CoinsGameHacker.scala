package contest

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 9/12/13
 * Time: 9:21
 * To change this template use File | Settings | File Templates.
 */
object CoinsGameHacker {
  def min(a: Int, b: Int ) = a min b
  def P(N: Int, K: Int, C: Int) = {
    val h = (C - 1) / N + 1;
    val full = min(N, K / h);
    val err = N - full;
    val ans = C + err;
    ans
  }
  def apply(N: Int, K: Int, C: Int) = P(N,K,C)

}
