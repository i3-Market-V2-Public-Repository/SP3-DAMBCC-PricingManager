package com.gft.test.formula;

import javax.script.ScriptEngineManager;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class EvaluationTest {

	public static void main(String[] args) {
		try 
		{
			//
			// USED ONLY FOR SANDBOX
			//
			
			// METODO 1
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			String foo = "40+2";
		    Object ret = engine.eval(foo);
		    
			System.out.println(ret);
			
			// METODO 2
			// https://mathparser.org/mxparser-tutorial/
			Expression e = new Expression("( 2 + 3/4 + sin(pi) )/2");
			double v = e.calculate();
			System.out.println(v);
			
			Function f = new Function("f(x, y, z) = sin(x) + cos(y*z)");
			
			Integer numArgs = f.getArgumentsNumber();
			for(int i= 0 ; i<numArgs; i++)
			{
				Argument argument = f.getArgument(i);
				System.out.println(argument.getArgumentName());
				argument.setArgumentValue(1);
			}
			Double db = f.calculate();
			System.out.println(db);	
			
			Function f1 = new Function("sin(x) + cos(y*z)");
			
			
			Integer numArgs1 = f1.getArgumentsNumber();
			for(int i= 0 ; i<numArgs1; i++)
			{
				Argument argument = f1.getArgument(i);
				System.out.println(argument.getArgumentName());
				argument.setArgumentValue(1);
			}
			Double db1 = f1.calculate();
			System.out.println(db);	
			
		} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	}

}
