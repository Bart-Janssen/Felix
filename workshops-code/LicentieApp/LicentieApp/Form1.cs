using System;
using System.Windows.Forms;

namespace LicentieApp
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void buttonCheckLicence_Click(object sender, EventArgs e)
        {
            if (textBoxLicence.Text == getLicenceFromServer())
            {
                MessageBox.Show("Activation succeed!");
                return;
            }
            MessageBox.Show("Wrong key, try again.");
        }

        private String getLicenceFromServer()
        {
            //Get licence from some url, for example static licence is used.
            return "SB2AA-59NDP-HMF7M-8ZF8T-PWJAR";
        }
    }
}