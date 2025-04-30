import os
import time
from PyPDF2 import PdfReader
import re

def count_and_sum_transactions_in_pdfs(folder_path, transaction_name):
    total_mta_count = 0
    total_amount_spent = 0.0
    start_time = time.time()

    # Get all the PDFs in folder
    pdf_files = [f for f in os.listdir(folder_path) if f.lower().endswith('.pdf')]
    total_files = len(pdf_files)

    if total_files == 0:
        print("No PDF files found in the specified folder.")
        return

    print(f"Found {total_files} PDF files. Starting scan...")

    # Process each PDF
    for i, pdf_file in enumerate(pdf_files):
        pdf_path = os.path.join(folder_path, pdf_file)
        try:
            reader = PdfReader(pdf_path)

            for page in reader.pages:
                text = page.extract_text()
                lines = text.split("\n")
                
                for idx, line in enumerate(lines):
                    if transaction_name in line:
                        total_mta_count += 1

                        # Check subsequent lines for the amount
                        for lookahead_idx in range(idx + 1, len(lines)):
                            next_line = lines[lookahead_idx].strip()
                            if re.match(r"^\d+\.\d{2}$", next_line):
                                total_amount_spent += float(next_line)
                                break

        except Exception as e:
            print(f"Error reading {pdf_file}: {e}")

        elapsed_time = int(time.time() - start_time)

    # Final output
    print("Scan Complete.")
    elapsed_time = time.time() - start_time
    print(f"Processed {total_files} PDF files in {int(elapsed_time // 60)}:{int(elapsed_time % 60):02d}.")
    print(f"Total '{transaction_name}' transactions found: {total_mta_count}")
    print(f"Total amount spent on '{transaction_name}' transactions: ${total_amount_spent:.2f}")

folder_path = "Tax\Bank Statments"  # Replace with your folder path
transaction_name = "MTA" # Input transaction name. MUST BE UNIQUE <<<
count_and_sum_transactions_in_pdfs(folder_path, transaction_name)