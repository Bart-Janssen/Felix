using System;
using System.Windows.Forms;

namespace LicentieApp
{
	// Token: 0x02000003 RID: 3
	internal static class Program
	{
		// Token: 0x06000006 RID: 6 RVA: 0x00002272 File Offset: 0x00000472
		[STAThread]
		private static void Main()
		{
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new Form1());
		}
	}
}
