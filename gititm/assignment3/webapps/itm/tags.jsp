<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="itm.image.*" %>
<%@ page import="itm.model.*" %>
<%@ page import="itm.util.*" %>
<%@ page import="javax.imageio.*" %>
<%@ page import="java.awt.*"%>
<%@ page import="java.awt.image.*"%>
<!--
/*******************************************************************************
 This file is part of the WM.II.ITM course 2014
 (c) University of Vienna 2009-2014
 *******************************************************************************/
-->
<%
       
%>
<html>
    <head>
    </head>
    <body>

        
        fill in your code here :)
        <%
        
            String tag = null;

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************

            // get "tag" parameter   
            if(request.getParameter("tag")!=null){
            tag = request.getParameter("tag"); }
            
            // if no param was passed, forward to index.jsp (using jsp:forward)
			if(request.getParameter("tag")==null){ %><jsp:forward page="index.jsp" /> 
        <% }
        %>

        <h1>Media that is tagged with <%= tag %></h1>
        <a href="index.jsp">back</a>

        <%

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************
        	ArrayList<AbstractMedia> media = MediaFactory.getMedia(tag);
        
            // get all media objects that are tagged with the passed tag
            for ( AbstractMedia medium : media ) {%>
            <div style="width:300px;height:300px;padding:10px;float:left;">
        <%
    
        // handle images
        if ( medium instanceof ImageMedia ) {
        	// display image thumbnail and metadata
            ImageMedia img = (ImageMedia) medium;
        	 
            %>
            <div style="width:200px;height:200px;padding:10px;">
                <a href="media/img/<%= img.getInstance().getName()%>">
                <img onmouseover="this.src='media/md/<%= img.getInstance().getName() %>.hist.png'; " onmouseout="this.src='media/md/<%= img.getInstance().getName() %>.thumb.png';" src="media/md/<%= img.getInstance().getName() %>.thumb.png" border="0" height="200px" width="200px"/>
                </a>
            </div>
            <div>
                Name: <%= img.getName() %><br/>
                Dimensions: <%= img.getWidht() %>x<%= img.getHeight() %>px<br/>
                Tags: <% for ( String t : img.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>
                
                
                
            </div>
            </div>
            <%  
            }	
        
            // iterate over all available media objects and display them
        }
        %>
        
    </body>
</html>
