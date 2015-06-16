<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="itm.image.*" %>
<%@ page import="itm.model.*" %>
<%@ page import="itm.util.*" %>

<!--
/*******************************************************************************
 This file is part of the WM.II.ITM course 2014
 (c) University of Vienna 2009-2014
 *******************************************************************************/
-->

<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
  <graph id="G" edgedefault="undirected">

    <key id="name" for="node" attr.name="name" attr.type="string"/>
    <key id="url" for="node" attr.name="url" attr.type="string"/>
    <key id="type" for="node" attr.name="type" attr.type="string"/>

    <node id="n0">
        <data key="type">concept</data>
        <data key="name">Tags</data>
        <data key="url">http://localhost:8080/itm/</data>
    </node>

    <%
        // get all media objects
        ArrayList<AbstractMedia> media = MediaFactory.getMedia();
        
        // List for storing tag-nodes
        Hashtable<String,String> tagNodes = new Hashtable<String,String>();

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************
        
        // iterate over all available media objects
       
        //Extrahiere alle unterschiedlichen Tags aus den Media Dateien
        //Die Unterscheidung braucht man eigentlich nicht.
        int c=0;
        for ( AbstractMedia medium : media ) {
            
            ArrayList<String> imgTags = media.get(c).getTags();
            	
            for(int i = 0; i < imgTags.size();i++){
            	if(!tagNodes.containsValue(imgTags.get(i))){
            		tagNodes.put(String.valueOf(imgTags.get(i).hashCode()), imgTags.get(i));
            	}
           	}
            	
            //System.out.println("HABE " + tagNodes.size() + " TAGS GESPEICHERT!");
            c++;
            }
        
        	
        	//Nachdem alle Tags extrahiert wurden k�nnen Nodes erstellt werden
        	// Nodes der Tags
        	int n = 1; // Da n0 schon der Root Node ist
        	
        	Enumeration<String> tags = tagNodes.elements();
        	while(tags.hasMoreElements()){ //Alle unterschiedlichen Tags durchgehen
        
        		String tagName = tags.nextElement();
        		%>
        		<node id="n<%= tagName %>"> 
      			<data key="type">concept</data>
        		<data key="name"><%=tagName %></data>
        		<data key="url">http://localhost:8080/itm/tags.jsp?tag=<%=tagName %></data>
    			
    			</node>
    			<edge id="e<%=n%>" directed="true" source="n0" target="n<%=tagName%>"></edge>
    			<%
    			n++;
        	}
        	
        	//Nodes der Media Dateien
        	int m = 0;
        	for ( AbstractMedia medium : media ) {
				
        		//Um die Dateien korrekt zu verlinken muss der Typ festgestellt werden.
        		String dataTyp = null;
        		if(medium instanceof ImageMedia ){
        			dataTyp = "img";
        		}
        		else if (medium instanceof AudioMedia ){
        			dataTyp = "audio";
        		}
				else if (medium instanceof VideoMedia ){
        			dataTyp = "video";
        		}
        		
        		%>
        		<node id="m<%=m%>">
      			<data key="type">node</data>
        		<data key="name"><%=medium.getName().toString() %></data>
        		<data key="url">http://localhost:8080/itm/media/<%= dataTyp %>/<%=medium.getName().toString() %></data>
				
				</node>
				<%//Kante zu jedem Tag 
				//Liste der Tags holen
				ArrayList<String> actTags = medium.getTags();
				
				//Sonderfall wenn keine Tags vorhanden
				
				if(actTags.size() == 0){
					%>
					<edge id="e<%=m%>" directed="true" source="m<%=m %>" target="n0"></edge>
					<%
				}
				else{
				
					for(int i = 0; i < actTags.size(); i++){
					%>
						<edge id="e<%=m + i%>" directed="true" source="m<%=m %>" target="n<%=actTags.get(i)%>"></edge>
			    		<%
					}
				}
        		m++;
        	}
    %>
        
     
  </graph>
</graphml>