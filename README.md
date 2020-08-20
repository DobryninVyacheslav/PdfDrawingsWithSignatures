<h1 align="center">PdfDrawingsWithSignatures</h1>

# Description
PdfDrawingsWithSignatures is an auxiliary application for Windchill that allows to save drawings in pdf representation,
and also adds the required names of people, their signatures, and dates to the title block of the drawing.
The application works in two modes: for one drawing and for a list of drawings.

Also in a separate branch there is the test Spring-implementation of this application.

## Saving one drawing
To implement this feature, a button has been added to Windchill using [savePdf.jsp](jsp/savePdf.jsp).

![](media/one_drw.gif)

## Saving list of drawings
To implement this feature, a button has been added to Windchill using [savePdfList.jsp](jsp/savePdfList.jsp).

![](media/list_of_drw.gif)
