//
//  TableTableViewController.swift
//  SampleIssuerApp
//
//  Created by Hirpara, Piyushkumar on 12/11/2020.
//  Copyright © 2020 Mastercard. All rights reserved.
//

import UIKit
import SafariServices

class ViewController: UITableViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Issuer App"
    }

    // MARK: - Table view data source
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return "Digital Card"
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CardCell", for: indexPath)
            
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        //Open card detail page in SFSafariViewController
        //Change the card detail page URL according to your server
        let safariViewController:SFSafariViewController? =  SFSafariViewController.init(url: URL(string: "<<Put Card HTML Page URL>>")!)
        safariViewController?.dismissButtonStyle = .close
        safariViewController?.preferredBarTintColor = .black
        safariViewController?.preferredControlTintColor = .white
        safariViewController?.modalPresentationStyle = .overFullScreen
        present(safariViewController!, animated: true, completion: nil)
    }

}
