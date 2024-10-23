import React, { useState, useEffect } from 'react';
import { Box, Heading, Input, Select, Table, Thead, Tbody, Tr, Th, Td, Button } from '@chakra-ui/react';
import axios from 'axios';

const AgreementsPage = () => {
  const [agreements, setAgreements] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');

  useEffect(() => {
    fetchAgreements();
  }, []);

  const fetchAgreements = async () => {
    try {
      const response = await axios.get('/api/agreements');
      setAgreements(response.data);
    } catch (error) {
      console.error('Error fetching agreements:', error);
    }
  };

  const filteredAgreements = agreements.filter(agreement =>
    agreement.name.toLowerCase().includes(searchTerm.toLowerCase()) &&
    (statusFilter === '' || agreement.status === statusFilter)
  );

  return (
    <Box p={5}>
      <Heading mb={6}>Agreements</Heading>
      <Box mb={4}>
        <Input
          placeholder="Search agreements"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          mb={2}
        />
        <Select
          placeholder="Filter by status"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option value="">All</option>
          <option value="PENDING">Pending</option>
          <option value="ACCEPTED">Accepted</option>
          <option value="REJECTED">Rejected</option>
        </Select>
      </Box>
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Agreement Name</Th>
            <Th>Status</Th>
            <Th>Actions</Th>
          </Tr>
        </Thead>
        <Tbody>
          {filteredAgreements.map((agreement) => (
            <Tr key={agreement.id}>
              <Td>{agreement.name}</Td>
              <Td>{agreement.status}</Td>
              <Td>
                <Button size="sm" colorScheme="blue" mr={2}>
                  View
                </Button>
                {agreement.status === 'PENDING' && (
                  <>
                    <Button size="sm" colorScheme="green" mr={2}>
                      Accept
                    </Button>
                    <Button size="sm" colorScheme="red">
                      Reject
                    </Button>
                  </>
                )}
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>
    </Box>
  );
};

export default AgreementsPage;
