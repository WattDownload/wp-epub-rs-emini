package us.wprust.jvm.components.menubar

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.formdev.flatlaf.extras.components.FlatButton
import com.formdev.flatlaf.extras.components.FlatButton.ButtonType
import raven.modal.ModalDialog
import raven.modal.component.SimpleModalBorder
import us.wprust.jvm.components.menubar.menu_panels.About
import us.wprust.jvm.components.menubar.menu_panels.Acknowledgements
import us.wprust.jvm.components.menubar.menu_panels.License
import us.wprust.jvm.utils.AppSettings.allowedConcurrencyValues
import us.wprust.jvm.utils.AppSettings.concurrentChapterCount
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.io.IOException
import java.net.URI
import javax.swing.*
import kotlin.system.exitProcess


object MenuBar {
    fun createMenuBar(frame: JFrame): JMenuBar {
        val menuBar = JMenuBar()

        val fileMenu = JMenu("File")
        fileMenu.setMnemonic('F')
        menuBar.add(fileMenu)

        val settingsItem = JMenuItem("Settings")
        settingsItem.setMnemonic('S')
        settingsItem.addActionListener { _: ActionEvent? -> settingsAction(frame) }
        fileMenu.add(settingsItem)

        fileMenu.add(JSeparator())

        val exitItem = JMenuItem("Exit")
        exitItem.setMnemonic('X')
        exitItem.addActionListener { _: ActionEvent? -> exitProcess(0) }
        fileMenu.add(exitItem)

        val helpMenu = JMenu("Help")
        helpMenu.setMnemonic('H')
        menuBar.add(helpMenu)

        val aboutItem = JMenuItem("About")
        aboutItem.setMnemonic('A')
        aboutItem.addActionListener { _: ActionEvent? ->
            ModalDialog.showModal(
                frame, SimpleModalBorder(About(), "About"), ModalDialog.createOption().setAnimationEnabled(true)
            )
        }
        helpMenu.add(aboutItem)

        helpMenu.add(JSeparator())

        val licenseItem = JMenuItem("License")
        licenseItem.setMnemonic('L')
        licenseItem.addActionListener { _: ActionEvent? ->
            ModalDialog.showModal(
                frame, SimpleModalBorder(License(), "License"), ModalDialog.createOption().setAnimationEnabled(true)
            )
        }
        helpMenu.add(licenseItem)

        val privacyItem = JMenuItem("Privacy Policy")
        privacyItem.setMnemonic('P')
        privacyItem.addActionListener { _: ActionEvent? ->
            try {
                Desktop.getDesktop()
                    .browse(URI.create("https://wpdl.us/application/singledl/desktop/java/help/privacy-policy"))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
//        helpMenu.add(privacyItem)

        val termsItem = JMenuItem("Terms of Service")
        termsItem.setMnemonic('T')
        termsItem.addActionListener { _: ActionEvent? ->
            try {
                Desktop.getDesktop()
                    .browse(URI.create("https://wpdl.us/application/singledl/desktop/java/help/terms-of-service"))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
//        helpMenu.add(termsItem)

        val acknowledgementsItem = JMenuItem("Acknowledgements")
        acknowledgementsItem.setMnemonic('K')
        acknowledgementsItem.addActionListener { _: ActionEvent? ->
            ModalDialog.showModal(
                frame,
                SimpleModalBorder(Acknowledgements(), "Acknowledgements"),
                ModalDialog.createOption().setAnimationEnabled(true)
            )
        }
        helpMenu.add(acknowledgementsItem)

        val settingsButton = FlatButton()
        settingsButton.icon =
            FlatSVGIcon("us/wprust/jvm/components/menubar/inlineSettings.svg")
        settingsButton.buttonType = ButtonType.toolBarButton
        settingsButton.isFocusable = false
        settingsButton.addActionListener { _: ActionEvent? -> settingsAction(frame) }
        menuBar.add(Box.createGlue())
        menuBar.add(settingsButton)

        return menuBar
    }

    private fun settingsAction(frame: JFrame) {
        frame.isAlwaysOnTop = false

        val selected = JOptionPane.showInputDialog(
            null,
            "Set Concurrent Chapter Count:",
            "Concurrency Settings",
            JOptionPane.INFORMATION_MESSAGE,
            null,
            allowedConcurrencyValues,
            concurrentChapterCount
        )

        if (selected != null) {
            val chosen = selected as UInt
            concurrentChapterCount = chosen
            JOptionPane.showMessageDialog(null, "Concurrent chapter count set to $chosen.")
        }

        frame.isAlwaysOnTop = false
    }
}