package scalaparser

import org.parboiled2.ParseError
import utest._
import utest.framework.Test
import utest.util.Tree

import scala.util.{Failure, Success}

object SyntaxTest extends TestSuite{
  def check[T](input: String) = {
    new ScalaSyntax(input).CompilationUnit.run() match{
      case Failure(f: ParseError) =>
        println(f.position)
        println(f.formatExpectedAsString)
        println(f.formatTraces)
        throw new Exception(f.position + "\t" + f.formatTraces)
      case Success(parsed) =>
        assert(parsed == input)
    }
  }
  def tests = TestSuite{
    'unit {
      * - check(
        "package torimatomeru"

      )
      * - check(
        """
          |package torimatomeru
          |
          |import org.parboiled2.ParseError
          |import utest._
          |import utest.framework.Test
        """.stripMargin

      )
      * - check(
        """
          |package torimatomeru
          |
          |import org.parboiled2.ParseError
          |import utest._
          |import utest.framework.Test
          |import utest.util.Tree
          |
          |import scala.util.{Failure, Success}
          |
          |object SyntaxTest extends TestSuite
        """.stripMargin
      )
      * - check(
        """
          |object SyntaxTest extends TestSuite{
          |  def check[T](input: String) = {
          |
          |  }
          |}
        """.stripMargin
      )
      * - check(
        """
          |object SyntaxTest{
          |  a()
          |  throw 1
          |}
        """.stripMargin
      )
      * - check(
        """
          |object SyntaxTest extends TestSuite{
          |  def check[T](input: String) = {
          |    new ScalaSyntax(input).CompilationUnit.run() match{
          |      case Failure(f: ParseError) =>
          |        println(f.position)
          |        println(f.formatExpectedAsString)
          |        println(f.formatTraces)
          |        throw new Exception(f.position + "\t" + f.formatTraces)
          |      case Success(parsed) =>
          |        assert(parsed == input)
          |    }
          |  }
          |}
        """.stripMargin
      )
      * - check(
        """package scalatex
          |
          |
          |import org.parboiled2._
          |import torimatomeru.ScalaSyntax
          |
          |import scalatex.stages.{Trim, Parser, Ast}
          |import scalatex.stages.Ast.Block.{IfElse, For, Text}
          |import Ast.Chain.Args
          |
          |object ParserTests extends utest.TestSuite{
          |  import Ast._
          |  import utest._
          |  def check[T](input: String, parse: Parser => scala.util.Try[T], expected: T) = {
          |    val parsed = parse(new Parser(input)).get
          |    assert(parsed == expected)
          |  }
          |  def tests = TestSuite{}
          |}
        """.stripMargin
      )
      * - check(
        """
          |object Moo{
          |  a
          |  .b
          |
          |  c
          |}
        """.stripMargin
      )
      * - check(
        """
          |object Moo{
          | filename
          |        .asInstanceOf[Literal]
          |10
          |}
        """.stripMargin
      )
      * - check(
        """
          |object Cow{
          |  ().mkString
          |
          |  1
          |}
        """.stripMargin
      )
      * - check(
        """
          |object O{
          | private[this] val applyMacroFull = 1
          |}
        """.stripMargin
      )
      * - check(
        """
          |object O{
          | private[this] def applyMacroFull(c: Context)
          |                      (expr: c.Expr[String],
          |                       runtimeErrors: Boolean,
          |                       debug: Boolean)
          |                      : c.Expr[Frag] = {
          |                      }
          |}
        """.stripMargin
      )
      * - check(
        """
          |object O{
          |  class DebugFailure extends Exception
          |
          |  1
          |}
        """.stripMargin
      )
      * - check(
        """
          |package torimatomeru
          |
          |package syntax
          |
          |import org.parboiled2._
          |
        """.stripMargin
      )
      * - check(
        """
          |object Foo{
          |  0 match {
          |    case A | B => 0
          |  }
          |}
        """.stripMargin
      )
      * - check(
      """
        |object Compiler{
        |
        |  def apply = {
        |    def rec = t match {
        |      case 0 => 0
        |    }
        |
        |    rec(tree)
        |  }
        |}
        |
      """.stripMargin
      )
      * - check(
        """
          |object O {
          |    A(A(A(A(A(A(A(A())))))))
          |}
          |
        """.stripMargin
      )
      * - check(
        """
          |object O{
          |   A(A(A(A(A(A(A(A(A(A(A(A(A(A(A(A())))))))))))))))
          |}
        """.stripMargin
      )
      * - check(
        """
          |object L{
          |  a.b = c
          |  a().b = c
          |}
        """.stripMargin
      )
      * - check(
        """/*                     __                                               *\
          |**     ________ ___   / /  ___      __ ____  Scala.js CLI               **
          |**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013-2014, LAMP/EPFL   **
          |**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
          |** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
          |**                          |/____/                                     **
          |\*                                                                      */
          |
          |package scala.scalajs.cli
          |
        """.stripMargin
      )
      * - check(
        """
          |object O{
          |  for {
          |      a  <- b
          |      c <- d
          |  } {
          |    1
          |  }
          |}
        """.stripMargin
      )
      * - check(
        """
          |object O{
          |  val jarFile =
          |      try { 1 }
          |      catch { case _: F => G }
          |}
        """.stripMargin
      )
      * - check(
        """
          |object F{
          |  func{ case _: F => fail }
          |}
        """.stripMargin
      )
      * - check(
        """
          |object Foo{
          |    val a = d // g
          |    val b = e // h
          |    val c = f
          |}
        """.stripMargin
      )
      * - check(
        """
          |object L{
          |  x match{
          |    case y.Y(z) => z
          |  }
          |}
        """.stripMargin
      )
      * - check(
        """object K{
          |  val a: B {
          |    val c: D
          |  }
          |
          |  1
          |}
        """.stripMargin
      )
      * - check(
        """
          |object LOLS{
          |    def run() {}
          |
          |    def apply() {}
          |}
        """.stripMargin
      )
      * - check(
        """
          |object O{
          |  a =:= b.c
          |}
        """.stripMargin
      )
    }
    def checkFile(path: String) = check(io.Source.fromFile(path).mkString)
    'file{


      * - checkFile("scalatexApi/src/main/scala/scalaparser/syntax/Basic.scala")
      * - checkFile("scalatexApi/src/main/scala/scalaparser/syntax/Identifiers.scala")
      * - checkFile("scalatexApi/src/main/scala/scalaparser/syntax/Literals.scala")
      * - checkFile("scalatexApi/src/main/scala/scalaparser/ScalaSyntax.scala")

      * - checkFile("scalatexApi/src/test/scala/scalaparser/SyntaxTest.scala")


      * - checkFile("scalatexApi/src/main/scala/scalatex/stages/Compiler.scala")
      * - checkFile("scalatexApi/src/main/scala/scalatex/stages/Parser.scala")
      * - checkFile("scalatexApi/src/main/scala/scalatex/stages/Trim.scala")
      * - checkFile("scalatexApi/src/main/scala/scalatex/package.scala")

      * - checkFile("scalatexApi/src/test/scala/scalatex/ParserTests.scala")
      * - checkFile("scalatexApi/src/test/scala/scalatex/BasicTests.scala")
      * - checkFile("scalatexApi/src/test/scala/scalatex/ErrorTests.scala")
      * - checkFile("scalatexApi/src/test/scala/scalatex/TestUtil.scala")

      * - checkFile("scalatexPlugin/src/main/scala/scalatex/ScalaTexPlugin.scala")
    }

    'omg{
      val root = new java.io.File("../scala-js/")
      def listFiles(s: java.io.File): Iterator[String] = {
        val (dirs, files) = s.listFiles().toIterator.partition(_.isDirectory)
        files.map(_.getPath) ++ dirs.flatMap(listFiles)
      }
      for(f <- listFiles(root).filter(_.endsWith(".scala"))){
        println("CHECKING " + f)
        checkFile(f)
      }
    }
  }
}
