namespace LicentieApp
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.buttonCheckLicence = new System.Windows.Forms.Button();
            this.textBoxLicence = new System.Windows.Forms.TextBox();
            this.SuspendLayout();
            // 
            // buttonCheckLicence
            // 
            this.buttonCheckLicence.Location = new System.Drawing.Point(168, 91);
            this.buttonCheckLicence.Name = "buttonCheckLicence";
            this.buttonCheckLicence.Size = new System.Drawing.Size(75, 23);
            this.buttonCheckLicence.TabIndex = 0;
            this.buttonCheckLicence.Text = "Check";
            this.buttonCheckLicence.UseVisualStyleBackColor = true;
            this.buttonCheckLicence.Click += new System.EventHandler(this.buttonCheckLicence_Click);
            // 
            // textBoxLicence
            // 
            this.textBoxLicence.Location = new System.Drawing.Point(155, 65);
            this.textBoxLicence.Name = "textBoxLicence";
            this.textBoxLicence.Size = new System.Drawing.Size(100, 20);
            this.textBoxLicence.TabIndex = 1;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(400, 214);
            this.Controls.Add(this.textBoxLicence);
            this.Controls.Add(this.buttonCheckLicence);
            this.Name = "Form1";
            this.Text = "Licence checker";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button buttonCheckLicence;
        private System.Windows.Forms.TextBox textBoxLicence;
    }
}

