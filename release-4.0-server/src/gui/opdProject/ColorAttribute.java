package gui.opdProject;
import java.awt.Color;
import java.util.StringTokenizer;

public class ColorAttribute implements Cloneable
{
    private String name;
    private Color myColor;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private String openingFontTag;
    private String closingFontTag;

    public ColorAttribute(String name,Color myColor, boolean bold, boolean italic, boolean underlined)
    {
        this.name = name;
        this.myColor = myColor;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.calculateFontTags();
    }

    protected Object clone()
    {
        return new ColorAttribute(this.name, this.myColor, this.bold, this.italic, this.underlined);
    }

    public String getName()
    {
        return this.name;
    }

    public String getStyleName()
    {
        StringTokenizer st = new StringTokenizer(this.name);
        return st.nextToken();
    }

    public void setColor(Color myColor)
    {
        this.myColor = myColor;
        this.calculateFontTags();
    }

    public Color getColor()
    {
        return this.myColor;
    }

    public void setBold(boolean bold)
    {
        this.bold = bold;
        this.calculateFontTags();
    }

    public boolean isBold()
    {
        return this.bold;
    }

    public void setItalic(boolean italic)
    {
        this.italic = italic;
        this.calculateFontTags();
    }

    public boolean isItalic()
    {
        return this.italic;
    }

    public void setUnderlined(boolean underlined)
    {
        this.underlined = underlined;
        this.calculateFontTags();
    }

    public boolean isUnderlined()
    {
        return this.underlined;
    }


    private String getRGB()
    {
        return this.toHex(this.myColor.getRed())+this.toHex(this.myColor.getGreen())+this.toHex(this.myColor.getBlue());
    }

    public String getStyleTag()
    {
        String tag = " ."+this.getStyleName()+" {\n";

        if (this.isBold())
        {
            tag += "font-weight : bold;\n";
        }
        else
        {
            tag += "font-weight : normal;\n";
        }

        if (this.isItalic())
        {
            tag += "font-style : italic;\n";
        }
        else
        {
            tag += "font-style : normal;\n";
        }

        if (this.isUnderlined())
        {
            tag += "text-decoration : underline;\n";
        }
        else
        {
            tag += "text-decoration : normal;\n";
        }


        tag += "color : #"+this.getRGB()+";\n";
        tag += "}\n";

        return tag;
    }



    public String openingHTMLStyleTag()
    {
        return "<font class=\""+this.getStyleName()+"\">";
    }

    public String closingHTMLStyleTag()
    {
        return "</font>";
    }

    public String openingHTMLFontTag()
    {
        return this.openingFontTag;
    }

    private void calculateFontTags()
    {
      this.openingFontTag = "<font color=\"#"+ this.getRGB()+"\">";
      this.closingFontTag = "</font>";
      if (this.isBold())
      {
        this.openingFontTag+="<B>";
        this.closingFontTag="</B>"+this.closingFontTag;
      }
      if (this.isItalic())
      {
        this.openingFontTag+="<I>";
        this.closingFontTag="</I>"+this.closingFontTag;
      }
      if (this.isUnderlined())
      {
        this.openingFontTag+="<U>";
        this.closingFontTag="</U>"+this.closingFontTag;
      }
    }

    public String closingHTMLFontTag()
    {
        return this.closingFontTag;
    }


    private String toHex(int num)
    {
        String s = Integer.toHexString(num);

        if (s.length() == 1)
        {
            return "0"+s;
        }
        return s;
    }

    public String toString()
    {
        return this.name;
    }
}