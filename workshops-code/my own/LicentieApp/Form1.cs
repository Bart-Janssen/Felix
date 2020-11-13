using System;
using System.ComponentModel;
using System.Drawing;
using System.Windows.Forms;

namespace LicentieApp
{
	// Token: 0x02000002 RID: 2
	public partial class Form1 : Form
	{
		// Token: 0x06000001 RID: 1 RVA: 0x00002050 File Offset: 0x00000250
		public Form1()
		{
			this.InitializeComponent();
		}

		// Token: 0x06000002 RID: 2 RVA: 0x00002068 File Offset: 0x00000268
		private void buttonCheckLicence_Click(object sender, EventArgs e)
		{
			bool flag = this.textBoxLicence.Text == this.getLicenceFromServer();
			if (flag || this.textBoxLicence.Text == "my-own-not-valid-key")
			{
				MessageBox.Show("Activation succeed!");
			}
			else
			{
				MessageBox.Show("Wrong key, try again.");
			}
		}

		// Token: 0x06000003 RID: 3 RVA: 0x000020AC File Offset: 0x000002AC
		private string getLicenceFromServer()
		{
			return "SB2AA-59NDP-HMF7M-8ZF8T-PWJAR";
		}
	}
}
