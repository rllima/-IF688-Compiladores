# mavenWithImp

O arquivo .g4 est� na pasta src/main/antlr4 ao finalizar sua implementa��o, basta salvar, bot�o direito no projeto -> run as -> maven generate-sources.

Os arquivos gerados estar�o na pasta target/generated-sources/antlr4. Basta copi�-los para dentro de um package, por exemplo, src/test/java/mavenWithImp 
e utiliz�-los. No caso, dentro desse package, est� um teste simples que le um arquivo de entrada com os exemplos de programa definidos no artigo e verificar se h� erros sint�ticos.