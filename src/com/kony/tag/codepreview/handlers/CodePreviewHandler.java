package com.kony.tag.codepreview.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.StatusTextEditor;
import org.apache.commons.codec.binary.Base64;

import com.kony.tag.util.TAGToolsUtil;

public class CodePreviewHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		
    	Shell shell = HandlerUtil.getActiveShell(event);
        try {               

        	IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
 
        	if(part instanceof IReusableEditor){
                System.out.println("is of type reusable editor");

                final IReusableEditor editor = (IReusableEditor)part;

                if(editor instanceof AbstractTextEditor){
                    System.out.println("abstract");
                }
                if(editor instanceof StatusTextEditor){
                    System.out.println("status");
                }
                if(editor instanceof TextEditor){
                    System.out.println("text");
                }
                if(editor instanceof AbstractDecoratedTextEditor){
                    System.out.println("abs dec");

                    AbstractDecoratedTextEditor adtEditor = (AbstractDecoratedTextEditor)editor;
                    IEditorInput input = adtEditor.getEditorInput();

                	if(input == null){
                    	System.out.println("Editor input is null");
                    }
                	
                    IDocumentProvider provider = adtEditor.getDocumentProvider();

                    IDocument document = provider.getDocument(input);

                    if (document == null) {
                           System.out.println("Document is null");
                           return null;
                    }
                    
                    
                    String editorText = document.get();
                    //replace function abc() with abc = function()
            		Pattern p = Pattern.compile("(function *)([a-zA-Z0-9 ]+)");
            		Matcher m = p.matcher(editorText);
            		String modifiedText = m.replaceAll("$2 = function");

            		String base64Str = new String(Base64.encodeBase64(modifiedText.getBytes()));
                    String decodedStr = new String(Base64.decodeBase64(base64Str.getBytes()));
                    System.out.println("Text in editor:");
                    System.out.println(editorText);
                    System.out.println("Modified text:");
                    System.out.println(modifiedText);
                    System.out.println("Base64 encoded text in editor:");
                    System.out.println(base64Str);
                    System.out.println("Base64 decoded text in editor:");
                    System.out.println(decodedStr);
                    
                    // read the i18n.properties
                    String projectPath = TAGToolsUtil.getCurrentSelectedProjectPath();
                    System.out.print("Current Project Path:" + projectPath);
                    
                    //TODO: Jetty server port should be read from the settings
                    String lPortNumber = null;
                    InputDialog lInputDialog = new InputDialog(shell, "Code Preview", "Enter the Port Number", "9999", null);
                    if(lInputDialog.open() == Window.OK)
                    {
                    	lPortNumber = lInputDialog.getValue();
                    	System.out.println("OK Pressed: " + lPortNumber);
	                    System.out.println("Entered Port Number: " + lPortNumber);
	                    URL  url = new URL("http://localhost:" + lPortNumber + "/tagcodepreview/savecode.jsp");
	                    String urlParameters = "encodedCode=" + base64Str;
	                    //urlParameters = "encodedResourceBundle=" + base64Str;
	                    HttpURLConnection  connection = (HttpURLConnection )url.openConnection();
	                    connection.setDoOutput(true); 
	                    connection.setInstanceFollowRedirects(false);
	                    
	                    OutputStreamWriter  wr = new OutputStreamWriter(connection.getOutputStream ());
	                    wr.write(urlParameters);
	                    wr.flush();
	 
	                    BufferedReader in = new BufferedReader(
	                                            new InputStreamReader(
	                                            connection.getInputStream()));
	                    String inputLine;
	
	                    while ((inputLine = in.readLine()) != null) 
	                        System.out.println(inputLine);
	                    wr.close();
	                    in.close();
	                    connection.disconnect();
                    }
                }               
          }

        } catch ( Exception ex ) {
            ex.printStackTrace();
            StringWriter sr = new StringWriter();
            PrintWriter pr = null;
            try{
            	pr = new PrintWriter(sr);
            }catch(Exception nex){
            	// show concise message and return
                MessageDialog.openInformation(shell, "Error", ex.toString());
                return null;
            }
            ex.printStackTrace(pr);
            pr.flush();
            pr.close();
            String exceptionMessage = ex.toString() + "\\n" + sr.toString();
            MessageDialog.openInformation(shell, "Error", exceptionMessage);
            return null;
        }
        MessageDialog.openInformation(shell, "Success", "Successfully published the code.");
		return null;
	}

}
