package com.atguigu.sparktuning.utils

import org.apache.yetus.audience.InterfaceAudience.Public


trait Test[T] {
  var a:T = _
//  def test[K](num:T, num2:K)

  def test1[V](num:V)
}

abstract class Test2 {
  //  def test[K](num:T, num2:K)

  def test1[k](num:k)
}

object test {
  def sample(function: Test2): Unit = {
    function.test1(1)
  }
  def main(args: Array[String]): Unit = {

    test.sample(new Test[Int](num: Int) => {
      println(num,num.getClass)
    })

//
//    test.sample(new Test[Int] {
//      def test1[K](num: Int, num2: K) {
//        println(num + num2)
//      }
//    })
  }
}
