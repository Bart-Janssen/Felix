namespace LicentieApp
{
	// Token: 0x02000002 RID: 2
	public partial class Form1 : global::System.Windows.Forms.Form
	{
		// Token: 0x06000004 RID: 4 RVA: 0x000020C4 File Offset: 0x000002C4
		protected override void Dispose(bool disposing)
		{
			bool flag = disposing && this.components != null;
			if (flag)
			{
				this.components.Dispose();
			}
			base.Dispose(disposing);
		}

		// Token: 0x06000005 RID: 5 RVA: 0x000020FC File Offset: 0x000002FC
		private void InitializeComponent()
		{
			this.buttonCheckLicence = new global::System.Windows.Forms.Button();
			this.textBoxLicence = new global::System.Windows.Forms.TextBox();
			base.SuspendLayout();
			this.buttonCheckLicence.Location = new global::System.Drawing.Point(168, 91);
			this.buttonCheckLicence.Name = "buttonCheckLicence";
			this.buttonCheckLicence.Size = new global::System.Drawing.Size(75, 23);
			this.buttonCheckLicence.TabIndex = 0;
			this.buttonCheckLicence.Text = "Check";
			this.buttonCheckLicence.UseVisualStyleBackColor = true;
			this.buttonCheckLicence.Click += new global::System.EventHandler(this.buttonCheckLicence_Click);
			this.textBoxLicence.Location = new global::System.Drawing.Point(155, 65);
			this.textBoxLicence.Name = "textBoxLicence";
			this.textBoxLicence.Size = new global::System.Drawing.Size(100, 20);
			this.textBoxLicence.TabIndex = 1;
			base.AutoScaleDimensions = new global::System.Drawing.SizeF(6f, 13f);
			base.AutoScaleMode = global::System.Windows.Forms.AutoScaleMode.Font;
			base.ClientSize = new global::System.Drawing.Size(400, 214);
			base.Controls.Add(this.textBoxLicence);
			base.Controls.Add(this.buttonCheckLicence);
			base.Name = "Form1";
			this.Text = "Licence checker";
			base.ResumeLayout(false);
			base.PerformLayout();
		}

		// Token: 0x04000001 RID: 1
		private global::System.ComponentModel.IContainer components = null;

		// Token: 0x04000002 RID: 2
		private global::System.Windows.Forms.Button buttonCheckLicence;

		// Token: 0x04000003 RID: 3
		private global::System.Windows.Forms.TextBox textBoxLicence;
	}
}
