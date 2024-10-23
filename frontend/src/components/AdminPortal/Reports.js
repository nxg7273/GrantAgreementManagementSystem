import React, { useState } from 'react';
import { Box, Heading, VStack, HStack, Select, Button, Table, Thead, Tbody, Tr, Th, Td, useToast } from '@chakra-ui/react';
import axios from 'axios';

const Reports = () => {
  const [reportType, setReportType] = useState('');
  const [reportData, setReportData] = useState([]);
  const toast = useToast();

  const generateReport = async () => {
    try {
      const response = await axios.get(`/api/admin/reports/${reportType}`);
      setReportData(response.data);
    } catch (error) {
      console.error('Error generating report:', error);
      toast({
        title: 'Error',
        description: 'Failed to generate report',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const renderReportTable = () => {
    if (reportData.length === 0) return null;

    const headers = Object.keys(reportData[0]);

    return (
      <Table variant="simple" mt={6}>
        <Thead>
          <Tr>
            {headers.map((header) => (
              <Th key={header}>{header}</Th>
            ))}
          </Tr>
        </Thead>
        <Tbody>
          {reportData.map((row, index) => (
            <Tr key={index}>
              {headers.map((header) => (
                <Td key={`${index}-${header}`}>{row[header]}</Td>
              ))}
            </Tr>
          ))}
        </Tbody>
      </Table>
    );
  };

  return (
    <Box p={5}>
      <Heading mb={6}>Reports</Heading>
      <VStack align="stretch" spacing={4}>
        <HStack>
          <Select
            placeholder="Select report type"
            value={reportType}
            onChange={(e) => setReportType(e.target.value)}
          >
            <option value="agreement-status">Agreement Status Report</option>
            <option value="participant-interaction">Participant Interaction Report</option>
            <option value="grant-distribution">Grant Distribution Report</option>
          </Select>
          <Button colorScheme="blue" onClick={generateReport} isDisabled={!reportType}>
            Generate Report
          </Button>
        </HStack>
        {renderReportTable()}
      </VStack>
    </Box>
  );
};

export default Reports;
