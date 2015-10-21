package gui.opdProject;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

public class PageSetupData extends PageFormat
{
	private boolean prjName = true;
	private boolean opdName = true;
	private boolean pageNumber = true;
	private boolean prjCreator = false;
	private boolean printTime = false;
	private boolean centerView = true;
	private boolean fitToPage = false;
	private boolean doNotAlign = false;
	private int marginWidth = 36;
	private boolean applyed;

	public PageSetupData(){
		this.setOrientation(PageFormat.REVERSE_LANDSCAPE);
		Paper p = this.getPaper();
		double h = this.getWidth();
		double w = this.getHeight();

		p.setImageableArea(this.marginWidth, this.marginWidth,
										w-this.marginWidth*2, h-this.marginWidth*2);
		this.setPaper(p);

	}
	//public PrintProperties(){}
	public boolean isCenterView()
	{
		return this.centerView;
	}
	public void setCenterView(boolean centerView)
	{
		this.centerView = centerView;
	}
	public boolean isFitToPage()
	{
		return this.fitToPage;
	}
	public void setFitToPage(boolean fitToPage)
	{
		this.fitToPage = fitToPage;
	}
	public boolean isDoNotAlign()
	{
		return this.doNotAlign;
	}
	public void setDoNotAlign(boolean doNotAlign)
	{
		this.doNotAlign = doNotAlign;
	}
	public boolean isPageNumber()
	{
		return this.pageNumber;
	}
	public void setPageNumber(boolean pageNumber)
	{
		this.pageNumber = pageNumber;
	}
	public boolean isPrintTime()
	{
		return this.printTime;
	}
	public void setPrintTime(boolean printTime)
	{
		this.printTime = printTime;
	}
	public boolean isPrjCreator()
	{
		return this.prjCreator;
	}
	public void setPrjCreator(boolean prjCreator)
	{
		this.prjCreator = prjCreator;
	}
	public boolean isPrjName()
	{
		return this.prjName;
	}
	public void setPrjName(boolean prjName)
	{
		this.prjName = prjName;
	}
	public boolean isOpdName()
	{
		return this.opdName;
	}
	public void setOpdName(boolean opdName)
	{
		this.opdName = opdName;
	}
	public boolean isApplyed()
	{
		return this.applyed;
	}
	public void setApplyed(boolean applyed)
	{
		this.applyed = applyed;
	}
	public int getMarginWidth()
	{
		return this.marginWidth;
	}
	public void setMarginWidth(int marginWidth)
	{
		this.marginWidth = marginWidth;
	}
}