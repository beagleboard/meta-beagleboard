#!/bin/sh
/bin/systemctl stop storage-gadget-init.service
/bin/systemctl stop network-gadget-init.service
/bin/systemctl start storage-gadget-init.service
