package us.wprust.jvm.scenes

import com.formdev.flatlaf.FlatClientProperties
import com.formdev.flatlaf.extras.components.FlatButton
import com.formdev.flatlaf.extras.components.FlatComboBox
import com.formdev.flatlaf.extras.components.FlatLabel
import com.formdev.flatlaf.extras.components.FlatTextField
import us.wprust.jvm.Main.Companion.sliderPanel
import java.awt.Cursor
import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.SwingConstants

class CheckoutPanel : JPanel() {

    // Callback to notify the main window when checkout is triggered
    lateinit var onCheckout: (storyInput: String) -> Unit
    private val checkoutButton: FlatButton // Make the button a class property
    private val inputField = FlatTextField()
    private val logo = FlatLabel()

    init {
        // Use the base panel configuration from Main
        val basePanel = sliderPanel
        layout = basePanel.layout
        putClientProperty(FlatClientProperties.STYLE, basePanel.getClientProperty(FlatClientProperties.STYLE))

        val imageUrl = javaClass.getResource("logo.png")
        val imageBytes = imageUrl?.readBytes()
        logo.icon = ImageIcon(imageBytes)
        logo.setBounds((580 - 150) / 2, 60, 150, 150)
        add(logo)

        inputField.placeholderText = "Enter Wattpad story ID, story link, or part link"
        inputField.horizontalAlignment = SwingConstants.CENTER
        inputField.isRoundRect = true
        inputField.setBounds(30 + 25, 30 + 220, 580 - 60 - 50, 30)
        add(inputField)

        checkoutButton = FlatButton()
        checkoutButton.text = "Checkout"
        checkoutButton.buttonType = FlatButton.ButtonType.roundRect
        checkoutButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        checkoutButton.setBounds((580 - 200) / 2, 30 + 30 + 30 + 230, 200, 30)
        add(checkoutButton)

        // The action listener now just calls the callback
        checkoutButton.addActionListener {
            if (::onCheckout.isInitialized) {
                onCheckout(inputField.text)
            }
        }
    }

    /**
     * This method is called by Swing when the panel is added to a window.
     * It's the safe place to set the default button.
     */
    override fun addNotify() {
        super.addNotify() // Always call the super method first
        rootPane.defaultButton = checkoutButton
    }

    fun disableCheckout() {
        checkoutButton.isEnabled = false
        inputField.isEnabled = false
    }

    fun enableCheckout() {
        checkoutButton.isEnabled = true
        inputField.isEnabled = true
    }

    fun rest() {
        inputField.text = null
        checkoutButton.isEnabled = true
        inputField.isEnabled = true
    }
}