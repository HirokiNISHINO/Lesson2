package kut.compiler.parser.ast;

import kut.compiler.cgen.CodeGenerator;
import kut.compiler.cgen.type.Type;
import kut.compiler.error.SemanticErrorException;
import kut.compiler.tokenizer.Token;

/**
 * @author hnishino
 *
 */
public class AstUnaryOp extends AstNode
{
	private Token	token	;
	private AstNode expr	;
	
	
	/**
	 * 
	 */
	public AstUnaryOp(Token token, AstNode expr) {
		this.token 	= token;
		this.expr 	= expr;
	}
	

	
	/**
	 *
	 */
	@Override
	public String getTreeString(int indent) {
		String r 	=  this.getIndentedStringWithCR(indent, "AstUnaryOp:" + this.token.getLexeme())
					+ expr.getTreeString(indent + 1);
		
		return r;
	}

	


	@Override
	public void beforeCGEN(CodeGenerator gen) throws SemanticErrorException {
		this.expr.beforeCGEN(gen);
		
		Type etype = expr.getType(gen);
		
		
		switch(token.getKlazz()) {
		case '-':
			if (Type.INT.equals(etype)) {
				return;
			}
			break;
		
		case '!':
			//TODO: 以下に！演算子が適用される式の型がType.BOOLEANであることを確認するコードを追加せよ。
			//TODO: 上の'-'のコードを参考にせよ。
			break;
		}
		
		throw new SemanticErrorException("Invalid Operation. the type:" + etype.getTypeNameString() + " can not be performed the operation: '" + token.getLexeme());
	}



	@Override
	public void cgen(CodeGenerator gen) {
		
		switch(token.getKlazz()) {
		case '-':
			expr.cgen(gen);
			gen.appendCode("neg rax");
			break;
			

		case '!':
			expr.cgen(gen);
			gen.appendCode("xor rax, 1");
			gen.appendCode("and rax, 1");
			break;
			
		default:
			break;
		}
		
		return;
	}
	

	
	
	
	
	/**
	 *
	 */
	@Override
	public Type getType(CodeGenerator gen) {
		
		switch(token.getKlazz()) {
		case '-':
			return Type.INT;
			
		case '!':
			return Type.BOOLEAN;
			
		default:
			break;
		}
		
		throw new RuntimeException("a bug. the code shouldn't reach here.");
	}
	
}