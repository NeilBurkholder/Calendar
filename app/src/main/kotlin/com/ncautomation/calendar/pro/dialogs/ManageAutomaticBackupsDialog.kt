package com.ncautomation.calendar.pro.dialogs

import androidx.appcompat.app.AlertDialog
import com.ncautomation.calendar.pro.R
import com.ncautomation.calendar.pro.activities.SimpleActivity
import com.ncautomation.calendar.pro.databinding.DialogManageAutomaticBackupsBinding
import com.ncautomation.calendar.pro.extensions.config
import com.ncautomation.commons.dialogs.FilePickerDialog
import com.ncautomation.commons.extensions.*
import com.ncautomation.commons.helpers.ensureBackgroundThread
import java.io.File

class ManageAutomaticBackupsDialog(private val activity: SimpleActivity, onSuccess: () -> Unit) {
    private val binding by activity.viewBinding(DialogManageAutomaticBackupsBinding::inflate)
    private val config = activity.config
    private var backupFolder = config.autoBackupFolder
    private var selectedEventTypes = config.autoBackupEventTypes.ifEmpty { config.displayEventTypes }

    init {
        binding.apply {
            backupEventsFolder.setText(activity.humanizePath(backupFolder))
            val filename = config.autoBackupFilename.ifEmpty {
                "${activity.getString(R.string.events)}_%Y%M%D_%h%m%s"
            }

            backupEventsFilename.setText(filename)
            backupEventsFilenameHint.setEndIconOnClickListener {
                DateTimePatternInfoDialog(activity)
            }

            backupEventsFilenameHint.setEndIconOnLongClickListener {
                DateTimePatternInfoDialog(activity)
                true
            }

            backupEventsCheckbox.isChecked = config.autoBackupEvents
            backupEventsCheckboxHolder.setOnClickListener {
                backupEventsCheckbox.toggle()
            }

            backupTasksCheckbox.isChecked = config.autoBackupTasks
            backupTasksCheckboxHolder.setOnClickListener {
                backupTasksCheckbox.toggle()
            }

            backupPastEventsCheckbox.isChecked = config.autoBackupPastEntries
            backupPastEventsCheckboxHolder.setOnClickListener {
                backupPastEventsCheckbox.toggle()
            }

            backupEventsFolder.setOnClickListener {
                selectBackupFolder()
            }

            manageEventTypesHolder.setOnClickListener {
                SelectEventTypesDialog(activity, selectedEventTypes) {
                    selectedEventTypes = it
                }
            }
        }
        activity.getAlertDialogBuilder()
            .setPositiveButton(com.ncautomation.commons.R.string.ok, null)
            .setNegativeButton(com.ncautomation.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, com.ncautomation.commons.R.string.manage_automatic_backups) { dialog ->
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val filename = binding.backupEventsFilename.value
                        when {
                            filename.isEmpty() -> activity.toast(com.ncautomation.commons.R.string.empty_name)
                            filename.isAValidFilename() -> {
                                val file = File(backupFolder, "$filename.ics")
                                if (file.exists() && !file.canWrite()) {
                                    activity.toast(com.ncautomation.commons.R.string.name_taken)
                                    return@setOnClickListener
                                }

                                val backupEventsChecked = binding.backupEventsCheckbox.isChecked
                                val backupTasksChecked = binding.backupTasksCheckbox.isChecked
                                if (!backupEventsChecked && !backupTasksChecked || selectedEventTypes.isEmpty()) {
                                    activity.toast(com.ncautomation.commons.R.string.no_entries_for_exporting)
                                    return@setOnClickListener
                                }

                                ensureBackgroundThread {
                                    config.apply {
                                        autoBackupFolder = backupFolder
                                        autoBackupFilename = filename
                                        autoBackupEvents = backupEventsChecked
                                        autoBackupTasks = backupTasksChecked
                                        autoBackupPastEntries = binding.backupPastEventsCheckbox.isChecked
                                        if (autoBackupEventTypes != selectedEventTypes) {
                                            autoBackupEventTypes = selectedEventTypes
                                        }
                                    }

                                    activity.runOnUiThread {
                                        onSuccess()
                                    }

                                    dialog.dismiss()
                                }
                            }

                            else -> activity.toast(com.ncautomation.commons.R.string.invalid_name)
                        }
                    }
                }
            }
    }

    private fun selectBackupFolder() {
        activity.hideKeyboard(binding.backupEventsFilename)
        FilePickerDialog(activity, backupFolder, pickFile = false, showFAB = true) { path ->
            activity.handleSAFDialog(path) { grantedSAF ->
                if (!grantedSAF) {
                    return@handleSAFDialog
                }

                activity.handleSAFDialogSdk30(path) { grantedSAF30 ->
                    if (!grantedSAF30) {
                        return@handleSAFDialogSdk30
                    }

                    backupFolder = path
                    binding.backupEventsFolder.setText(activity.humanizePath(path))
                }
            }
        }
    }
}

